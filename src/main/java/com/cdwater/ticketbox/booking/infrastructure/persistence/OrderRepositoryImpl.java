package com.cdwater.ticketbox.booking.infrastructure.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cdwater.ticketbox.booking.domain.model.Order;
import com.cdwater.ticketbox.booking.domain.model.OrderSeat;
import com.cdwater.ticketbox.booking.domain.repository.OrderRepository;
import com.cdwater.ticketbox.booking.infrastructure.persistence.mapper.OrderMapper;
import com.cdwater.ticketbox.booking.infrastructure.persistence.mapper.OrderSeatMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderMapper orderMapper;
    private final OrderSeatMapper orderSeatMapper;

    @Override
    public Order save(Order order) {
        orderMapper.insert(order);
        return order;
    }

    @Override
    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(orderMapper.selectById(id));
    }

    @Override
    public void saveSeats(List<OrderSeat> seats) {
        for (OrderSeat seat : seats) {
            orderSeatMapper.insert(seat);
        }
    }

    @Override
    public List<OrderSeat> findSeatsByOrderId(Long orderId) {
        return orderSeatMapper.selectList(new LambdaQueryWrapper<OrderSeat>()
                .eq(OrderSeat::getOrderId, orderId)
                .orderByAsc(OrderSeat::getId));
    }

    @Override
    public List<Order> findExpiredPending(LocalDateTime now, int limit) {
        int safeLimit = Math.max(1, Math.min(limit, 500));
        return orderMapper.selectList(new LambdaQueryWrapper<Order>()
                .eq(Order::getStatus, 0)
                .lt(Order::getExpireTime, now)
                .orderByAsc(Order::getId)
                .last("LIMIT " + safeLimit));
    }

    @Override
    public boolean markTimeout(Long orderId) {
        return orderMapper.markTimeout(orderId) == 1;
    }
}
