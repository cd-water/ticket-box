package com.cdwater.ticketbox.order.movie.application.command;

import com.cdwater.ticketbox.order.api.dto.CreateMovieOrderCommandDTO;
import com.cdwater.ticketbox.order.movie.application.port.MovieOrderNoGenerator;
import com.cdwater.ticketbox.order.movie.application.query.MovieOrderQueryService;
import com.cdwater.ticketbox.order.api.dto.MovieOrderDetailDTO;
import com.cdwater.ticketbox.order.movie.domain.model.MovieOrder;
import com.cdwater.ticketbox.order.movie.domain.model.MovieOrderSeat;
import com.cdwater.ticketbox.order.movie.domain.repository.MovieOrderRepository;
import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.ResultCode;
import com.cdwater.ticketbox.content.api.ContentCatalogApi;
import com.cdwater.ticketbox.content.api.dto.MovieBriefDTO;
import com.cdwater.ticketbox.content.api.dto.ScreeningBookingDTO;
import com.cdwater.ticketbox.inventory.api.dto.MovieSeatSnapshotDTO;
import com.cdwater.ticketbox.payment.api.PaymentApi;
import com.cdwater.ticketbox.payment.api.dto.PaymentDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieOrderCommandService {

    private final MovieOrderRepository orderRepository;
    private final MovieOrderNoGenerator orderNoGenerator;
    private final SeatInventoryService seatInventoryService;
    private final MovieOrderExpiryService orderExpiryService;
    private final MovieOrderQueryService orderQueryService;

    @DubboReference(check = false, timeout = 3000)
    private ContentCatalogApi contentCatalogApi;

    @DubboReference(check = false, timeout = 3000)
    private PaymentApi paymentApi;

    @Transactional
    public MovieOrderDetailDTO createOrder(Long userId, CreateMovieOrderCommandDTO command) {
        List<Long> rawSeatIds = command.getSeatIds();
        if (rawSeatIds == null || rawSeatIds.isEmpty() || rawSeatIds.size() > 6
                || rawSeatIds.stream().anyMatch(id -> id == null || id <= 0)) {
            throw new BizException(ResultCode.BAD_REQUEST, "请选择 1-6 个有效座位");
        }
        List<Long> seatIds = rawSeatIds.stream().distinct().toList();
        if (seatIds.size() != rawSeatIds.size()) {
            throw new BizException(ResultCode.BAD_REQUEST, "座位不能重复");
        }

        LocalDateTime now = LocalDateTime.now();
        ScreeningBookingDTO info = contentCatalogApi.getScreeningBooking(command.getScreeningId());
        MovieBriefDTO movie = contentCatalogApi.getMovie(info.getMovieId());
        if (movie.getStatus() == null || movie.getStatus() != 1) {
            throw new BizException(ResultCode.NOT_FOUND, "电影不存在或已下架");
        }

        long orderNo = orderNoGenerator.nextOrderNo();
        MovieOrder order = MovieOrder.place(orderNo, userId,
                info.getScreeningId(), movie.getTitle(), info.getCinemaName(), info.getHallName(),
                info.getStartTime(), info.getEndTime(), info.getPrice(), seatIds.size(), now);

        List<MovieSeatSnapshotDTO> snapshots = seatInventoryService.lockSeats(
                info.getScreeningId(), seatIds, orderNo, order.getExpireTime());
        try {
            orderRepository.save(order);
            orderRepository.saveSeats(MovieOrder.seatsOf(orderNo, snapshots, info.getPrice()));
        } catch (RuntimeException e) {
            seatInventoryService.releaseByOrder(orderNo);
            throw e;
        }

        return orderQueryService.getOrder(userId, orderNo);
    }

    @Transactional
    public MovieOrderDetailDTO pay(Long userId, Long orderNo) {
        MovieOrder order = requireOwned(userId, orderNo);
        LocalDateTime now = LocalDateTime.now();
        if (!order.isPending()) {
            throw new BizException(ResultCode.CONFLICT, "订单状态已变更");
        }
        if (order.isExpired(now)) {
            orderExpiryService.expireOrder(orderNo);
            throw new BizException(ResultCode.CONFLICT, "订单已关闭");
        }
        if (!orderRepository.markPaid(orderNo, now)) {
            throw new BizException(ResultCode.CONFLICT, "订单状态已变更");
        }
        List<MovieOrderSeat> seats = orderRepository.findSeatsByOrderNo(orderNo);
        PaymentDTO payment = paymentApi.create(orderNo, order.getTotalAmount(), "MOCK");
        int sold;
        try {
            sold = seatInventoryService.markSoldByOrder(orderNo);
        } catch (RuntimeException e) {
            safely(() -> seatInventoryService.rollbackSale(orderNo), "rollbackSale " + orderNo);
            safely(() -> orderRepository.markPending(orderNo), "markPending " + orderNo);
            safely(() -> paymentApi.close(payment.getPaymentNo()), "close payment " + payment.getPaymentNo());
            throw e;
        }
        if (sold != seats.size()) {
            safely(() -> seatInventoryService.rollbackSale(orderNo), "rollbackSale " + orderNo);
            safely(() -> orderRepository.markPending(orderNo), "markPending " + orderNo);
            safely(() -> paymentApi.close(payment.getPaymentNo()), "close payment " + payment.getPaymentNo());
            throw new BizException(ResultCode.CONFLICT, "座位已释放，请重新下单");
        }
        return orderQueryService.getOrder(userId, orderNo);
    }

    @Transactional
    public MovieOrderDetailDTO cancel(Long userId, Long orderNo) {
        requireOwned(userId, orderNo);
        PaymentDTO payment = paymentApi.queryByOrderNo(orderNo);
        if (payment != null && "SUCCESS".equals(payment.getStatus())) {
            throw new BizException(ResultCode.CONFLICT, "订单支付处理中，请稍后");
        }
        if (!orderRepository.markCancelled(orderNo)) {
            throw new BizException(ResultCode.CONFLICT, "订单状态已变更");
        }
        seatInventoryService.releaseByOrder(orderNo);
        return orderQueryService.getOrder(userId, orderNo);
    }

    @Transactional
    public void expireOrder(Long orderNo) {
        orderExpiryService.expireOrder(orderNo);
    }

    private void safely(Runnable action, String what) {
        try {
            action.run();
        } catch (RuntimeException ex) {
            log.warn("compensation failed: {}", what, ex);
        }
    }

    private MovieOrder requireOwned(Long userId, Long orderNo) {
        return orderRepository.findByOrderNo(orderNo)
                .filter(o -> o.getUserId().equals(userId))
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "订单不存在"));
    }
}