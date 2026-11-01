package com.cdwater.ticketbox.booking.domain.repository;

import com.cdwater.ticketbox.booking.domain.model.Order;
import com.cdwater.ticketbox.booking.domain.model.OrderSeat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findByOrderNo(Long orderNo);

    void saveSeats(List<OrderSeat> seats);

    List<OrderSeat> findSeatsByOrderNo(Long orderNo);

    List<Order> findExpiredPending(LocalDateTime now, int limit);

    boolean markTimeout(Long orderNo);
}
