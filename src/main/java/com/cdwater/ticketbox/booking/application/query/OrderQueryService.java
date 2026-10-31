package com.cdwater.ticketbox.booking.application.query;

import com.cdwater.ticketbox.booking.application.command.OrderExpiryService;
import com.cdwater.ticketbox.booking.application.port.MovieInfo;
import com.cdwater.ticketbox.booking.application.port.MovieInfoPort;
import com.cdwater.ticketbox.booking.application.port.ScreeningInfo;
import com.cdwater.ticketbox.booking.application.port.ScreeningInfoPort;
import com.cdwater.ticketbox.booking.application.query.mapper.OrderSeatViewMapper;
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

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderQueryService {

    private final OrderRepository orderRepository;
    private final ScreeningInfoPort screeningInfoPort;
    private final MovieInfoPort movieInfoPort;
    private final OrderExpiryService orderExpiryService;
    private final OrderSeatViewMapper orderSeatViewMapper;

    @Transactional
    public OrderDetailView getOrder(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .filter(o -> o.getUserId().equals(userId))
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "订单不存在"));

        if (order.getStatus() == OrderStatus.PENDING.getCode()
                && !order.getExpireTime().isAfter(LocalDateTime.now())) {
            orderExpiryService.expireOrder(orderId);
            order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "订单不存在"));
        }

        ScreeningInfo info = screeningInfoPort.get(order.getScreeningId());
        MovieInfo movie = movieInfoPort.findById(info.getMovieId())
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "电影不存在"));
        List<OrderSeat> seats = orderRepository.findSeatsByOrderId(orderId);

        OrderDetailView view = new OrderDetailView();
        view.setId(order.getId());
        view.setOrderNo(String.valueOf(order.getOrderNo()));
        view.setStatus(order.getStatus());
        view.setTotalAmount(order.getTotalAmount());
        view.setExpireTime(order.getExpireTime());
        view.setCreateTime(order.getCreateTime());
        view.setCinemaName(info.getCinemaName());
        view.setMovieTitle(movie.getTitle());
        view.setHallName(info.getHallName());
        view.setStartTime(info.getStartTime());
        view.setEndTime(info.getEndTime());
        view.setSeats(seats.stream()
                .map(orderSeatViewMapper::toView)
                .toList());
        return view;
    }
}
