package com.cdwater.ticketbox.play.domain.repository;

import com.cdwater.ticketbox.play.domain.model.PlayOrder;
import com.cdwater.ticketbox.shared.query.PageResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PlayOrderRepository {

    PlayOrder save(PlayOrder order);

    Optional<PlayOrder> findByOrderNo(Long orderNo);

    PageResult<PlayOrder> pageByUser(Long userId, int status, long page, long size);

    boolean markPaid(Long orderNo, LocalDateTime payTime);

    boolean markCancelled(Long orderNo);

    List<PlayOrder> findExpiredPending(LocalDateTime now, int limit);
}
