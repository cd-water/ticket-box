package com.cdwater.ticketbox.inventory.domain.repository;

import com.cdwater.ticketbox.inventory.domain.model.MovieSeatSnapshot;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface MovieSeatInventoryRepository {

    List<MovieSeatSnapshot> findSeatMap(Long screeningId);

    Map<Long, Long> countAvailableSeats(Collection<Long> screeningIds);

    List<MovieSeatSnapshot> lockSeats(Long screeningId, Collection<Long> seatIds);

    int updateLocks(Long screeningId, Collection<Long> seatIds, Long orderNo, LocalDateTime expireTime);

    List<MovieSeatSnapshot> findLockedByOrder(Long orderNo);

    int releaseByOrder(Long orderNo);

    int markSoldByOrder(Long orderNo);
}
