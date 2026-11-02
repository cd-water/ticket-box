package com.cdwater.ticketbox.inventory.domain.repository;

import com.cdwater.ticketbox.inventory.domain.model.SeatSnapshot;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface SeatInventoryRepository {

    List<SeatSnapshot> findSeatMap(Long screeningId);

    Map<Long, Long> countAvailable(Collection<Long> screeningIds);

    List<SeatSnapshot> lockSeats(Long screeningId, Collection<Long> seatIds);

    int updateLocks(Long screeningId, Collection<Long> seatIds, Long orderNo, LocalDateTime expireTime);

    int releaseByOrder(Long orderNo);

    int markSoldByOrder(Long orderNo);

    int rollbackSale(Long orderNo);

    int releaseExpiredByScreening(Long screeningId, LocalDateTime now);
}
