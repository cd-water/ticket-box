package com.cdwater.ticketbox.order.movie.domain.repository;

import com.cdwater.ticketbox.order.movie.domain.model.MovieOrder;
import com.cdwater.ticketbox.order.movie.domain.model.MovieOrderSeat;
import com.cdwater.ticketbox.common.result.PageResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MovieOrderRepository {

    MovieOrder save(MovieOrder order);

    Optional<MovieOrder> findByOrderNo(Long orderNo);

    PageResult<MovieOrder> pageByUser(Long userId, int status, long page, long size);

    void saveSeats(List<MovieOrderSeat> seats);

    List<MovieOrderSeat> findSeatsByOrderNo(Long orderNo);

    List<MovieOrder> findExpiredPending(LocalDateTime now, int limit);

    boolean markCancelled(Long orderNo);

    boolean markPaid(Long orderNo, LocalDateTime payTime);

    boolean markPending(Long orderNo);
}
