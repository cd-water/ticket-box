package com.cdwater.ticketbox.show.domain.repository;

import com.cdwater.ticketbox.show.domain.model.ShowOrder;
import com.cdwater.ticketbox.show.domain.model.ShowOrderSeat;
import com.cdwater.ticketbox.shared.query.PageResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShowOrderRepository {

    ShowOrder save(ShowOrder order);

    Optional<ShowOrder> findByOrderNo(Long orderNo);

    void saveSeats(List<ShowOrderSeat> seats);

    List<ShowOrderSeat> findSeatsByOrderNo(Long orderNo);

    PageResult<ShowOrder> pageByUser(Long userId, int status, long page, long size);

    boolean markPaid(Long orderNo, LocalDateTime payTime);

    boolean markCancelled(Long orderNo);

    List<ShowOrder> findExpiredPending(LocalDateTime now, int limit);
}
