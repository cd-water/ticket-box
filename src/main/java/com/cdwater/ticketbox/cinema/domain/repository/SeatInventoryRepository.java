package com.cdwater.ticketbox.cinema.domain.repository;

import com.cdwater.ticketbox.cinema.domain.model.SeatSnapshot;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface SeatInventoryRepository {

    List<SeatSnapshot> findSeatMap(Long screeningId);

    List<SeatSnapshot> lockSeats(Long screeningId, Collection<Long> seatIds);

    int updateLocks(Long screeningId, Collection<Long> seatIds, Long orderId, LocalDateTime expireTime);

    int releaseByOrder(Long orderId);

    int releaseExpiredByScreening(Long screeningId, LocalDateTime now);
}
