package com.cdwater.ticketbox.booking.application.query;

import com.cdwater.ticketbox.booking.application.command.OrderExpiryService;
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
    private final OrderExpiryService orderExpiryService;
    private final OrderSeatViewMapper orderSeatViewMapper;

    @Transactional
    public OrderDetailView getOrder(Long userId, Long orderNo) {
        Order order = orderRepository.findByOrderNo(orderNo)
                .filter(o -> o.getUserId().equals(userId))
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "订单不存在"));

        if (order.getStatus() == OrderStatus.PENDING.getCode()
                && !order.getExpireTime().isAfter(LocalDateTime.now())) {
            orderExpiryService.expireOrder(orderNo);
            order = orderRepository.findByOrderNo(orderNo)
                    .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "订单不存在"));
        }

        List<OrderSeat> seats = orderRepository.findSeatsByOrderNo(orderNo);

        OrderDetailView view = new OrderDetailView();
        view.setOrderNo(String.valueOf(order.getOrderNo()));
        view.setStatus(order.getStatus());
        view.setTotalAmount(order.getTotalAmount());
        view.setExpireTime(order.getExpireTime());
        view.setCreateTime(order.getCreateTime());
        view.setCinemaName(order.getCinemaName());
        view.setMovieTitle(order.getMovieTitle());
        view.setHallName(order.getHallName());
        view.setStartTime(order.getStartTime());
        view.setEndTime(order.getEndTime());
        view.setSeats(seats.stream()
                .map(orderSeatViewMapper::toView)
                .toList());
        return view;
    }
}
