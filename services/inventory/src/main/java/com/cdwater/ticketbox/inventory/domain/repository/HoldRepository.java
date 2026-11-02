package com.cdwater.ticketbox.inventory.domain.repository;

import com.cdwater.ticketbox.inventory.domain.model.Hold;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface HoldRepository {

    Optional<Hold> findByOrderNo(Long orderNo);

    void insert(Hold hold);

    int markConfirmed(Long orderNo);

    int markReleased(Long orderNo);

    List<Hold> findExpiredHeld(LocalDateTime now, int limit);
}
