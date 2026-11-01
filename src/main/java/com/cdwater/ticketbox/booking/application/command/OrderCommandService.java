package com.cdwater.ticketbox.booking.application.command;

import com.cdwater.ticketbox.booking.application.command.mapper.OrderSeatDomainMapper;
import com.cdwater.ticketbox.booking.application.command.CreateOrderCommand;
import com.cdwater.ticketbox.booking.application.port.MovieInfo;
import com.cdwater.ticketbox.booking.application.port.MovieInfoPort;
import com.cdwater.ticketbox.booking.application.port.OrderNoGenerator;
import com.cdwater.ticketbox.booking.application.port.ReservedSeat;
import com.cdwater.ticketbox.booking.application.port.ScreeningInfo;
import com.cdwater.ticketbox.booking.application.port.ScreeningInfoPort;
import com.cdwater.ticketbox.booking.application.port.SeatInventoryPort;
import com.cdwater.ticketbox.booking.application.query.OrderQueryService;
import com.cdwater.ticketbox.booking.application.query.view.OrderDetailView;
import com.cdwater.ticketbox.booking.domain.model.Order;
import com.cdwater.ticketbox.booking.domain.model.OrderSeat;
import com.cdwater.ticketbox.booking.domain.model.OrderStatus;
import com.cdwater.ticketbox.booking.domain.repository.OrderRepository;
import com.cdwater.ticketbox.shared.error.BizException;
import com.cdwater.ticketbox.shared.error.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderCommandService {

    private final OrderRepository orderRepository;
    private final OrderNoGenerator orderNoGenerator;
    private final ScreeningInfoPort screeningInfoPort;
    private final SeatInventoryPort seatInventoryPort;
    private final MovieInfoPort movieInfoPort;
    private final OrderExpiryService orderExpiryService;
    private final OrderQueryService orderQueryService;
    private final OrderSeatDomainMapper orderSeatDomainMapper;

    @Transactional
    public OrderDetailView createOrder(Long userId, CreateOrderCommand command) {
        List<Long> rawSeatIds = command.getSeatIds();
        if (rawSeatIds == null || rawSeatIds.isEmpty() || rawSeatIds.size() > 6
                || rawSeatIds.stream().anyMatch(id -> id == null || id <= 0)) {
            throw new BizException(ResultCode.BAD_REQUEST, "请选择 1-6 个有效座位");
        }
        List<Long> seatIds = rawSeatIds.stream().distinct().toList();
        if (seatIds.size() != rawSeatIds.size()) {
            throw new BizException(ResultCode.BAD_REQUEST, "座位不能重复");
        }

        ScreeningInfo info = screeningInfoPort.get(command.getScreeningId());
        if (info.getStatus() == null || info.getStatus() != 1
                || !info.getStartTime().isAfter(LocalDateTime.now())) {
            throw new BizException(ResultCode.NOT_FOUND, "排场不存在或已不可售");
        }
        MovieInfo movie = movieInfoPort.findById(info.getMovieId())
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "电影不存在或已下架"));
        if (movie.getStatus() == null || movie.getStatus() != 1) {
            throw new BizException(ResultCode.NOT_FOUND, "电影不存在或已下架");
        }

        LocalDateTime expireTime = LocalDateTime.now().plusMinutes(15);
        Order order = new Order();
        order.setOrderNo(orderNoGenerator.nextOrderNo());
        order.setUserId(userId);
        order.setScreeningId(info.getScreeningId());
        order.setMovieTitle(movie.getTitle());
        order.setCinemaName(info.getCinemaName());
        order.setHallName(info.getHallName());
        order.setStartTime(info.getStartTime());
        order.setEndTime(info.getEndTime());
        order.setTotalAmount(info.getPrice().multiply(BigDecimal.valueOf(seatIds.size())));
        order.setStatus(OrderStatus.PENDING.getCode());
        order.setExpireTime(expireTime);
        orderRepository.save(order);

        List<ReservedSeat> snapshots = seatInventoryPort.lockSeats(
                info.getScreeningId(), seatIds, order.getOrderNo(), expireTime);
        List<OrderSeat> orderSeats = snapshots.stream().map(snapshot -> {
            OrderSeat seat = orderSeatDomainMapper.toSeat(snapshot);
            seat.setOrderNo(order.getOrderNo());
            seat.setPrice(info.getPrice());
            return seat;
        }).toList();
        orderRepository.saveSeats(orderSeats);

        return orderQueryService.getOrder(userId, order.getOrderNo());
    }

    @Transactional
    public void expireOrder(Long orderNo) {
        orderExpiryService.expireOrder(orderNo);
    }
}
