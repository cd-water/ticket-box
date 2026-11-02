package com.cdwater.ticketbox.cinema.application.command;

import com.cdwater.ticketbox.cinema.application.command.CreateMovieOrderCommand;
import com.cdwater.ticketbox.cinema.application.port.MovieInfo;
import com.cdwater.ticketbox.cinema.application.port.MovieInfoPort;
import com.cdwater.ticketbox.cinema.application.port.MovieOrderNoGenerator;
import com.cdwater.ticketbox.cinema.application.query.MovieOrderQueryService;
import com.cdwater.ticketbox.cinema.application.query.ScreeningBookingInfo;
import com.cdwater.ticketbox.cinema.application.query.ScreeningQueryService;
import com.cdwater.ticketbox.cinema.application.query.view.MovieOrderDetailView;
import com.cdwater.ticketbox.cinema.domain.model.MovieOrder;
import com.cdwater.ticketbox.cinema.domain.model.MovieOrderSeat;
import com.cdwater.ticketbox.cinema.domain.model.Screening;
import com.cdwater.ticketbox.cinema.domain.model.SeatSnapshot;
import com.cdwater.ticketbox.cinema.domain.repository.MovieOrderRepository;
import com.cdwater.ticketbox.cinema.domain.repository.ScreeningRepository;
import com.cdwater.ticketbox.shared.error.BizException;
import com.cdwater.ticketbox.shared.error.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieOrderCommandService {

    private final MovieOrderRepository orderRepository;
    private final MovieOrderNoGenerator orderNoGenerator;
    private final ScreeningRepository screeningRepository;
    private final ScreeningQueryService screeningQueryService;
    private final SeatInventoryService seatInventoryService;
    private final MovieInfoPort movieInfoPort;
    private final MovieOrderExpiryService orderExpiryService;
    private final MovieOrderQueryService orderQueryService;

    @Transactional
    public MovieOrderDetailView createOrder(Long userId, CreateMovieOrderCommand command) {
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
        ScreeningBookingInfo info = screeningQueryService.getBookingInfo(command.getScreeningId());
        Screening screening = screeningRepository.findById(command.getScreeningId())
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "排场不存在或已不可售"));
        if (!screening.isBookable(now)) {
            throw new BizException(ResultCode.NOT_FOUND, "排场不存在或已不可售");
        }

        MovieInfo movie = movieInfoPort.findById(info.getMovieId())
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "电影不存在或已下架"));
        if (movie.getStatus() == null || movie.getStatus() != 1) {
            throw new BizException(ResultCode.NOT_FOUND, "电影不存在或已下架");
        }

        MovieOrder order = MovieOrder.place(orderNoGenerator.nextOrderNo(), userId,
                info.getScreeningId(), movie.getTitle(), info.getCinemaName(), info.getHallName(),
                info.getStartTime(), info.getEndTime(), info.getPrice(), seatIds.size(), now);
        orderRepository.save(order);

        List<SeatSnapshot> snapshots = seatInventoryService.lockSeats(
                info.getScreeningId(), seatIds, order.getOrderNo(), order.getExpireTime());
        orderRepository.saveSeats(MovieOrder.seatsOf(order.getOrderNo(), snapshots, info.getPrice()));

        return orderQueryService.getOrder(userId, order.getOrderNo());
    }

    @Transactional
    public MovieOrderDetailView pay(Long userId, Long orderNo) {
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
        if (seatInventoryService.markSoldByOrder(orderNo) != seats.size()) {
            throw new BizException(ResultCode.CONFLICT, "座位已释放，请重新下单");
        }
        return orderQueryService.getOrder(userId, orderNo);
    }

    @Transactional
    public MovieOrderDetailView cancel(Long userId, Long orderNo) {
        requireOwned(userId, orderNo);
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

    private MovieOrder requireOwned(Long userId, Long orderNo) {
        return orderRepository.findByOrderNo(orderNo)
                .filter(o -> o.getUserId().equals(userId))
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "订单不存在"));
    }
}
