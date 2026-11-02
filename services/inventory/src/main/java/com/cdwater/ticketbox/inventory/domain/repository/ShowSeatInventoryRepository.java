package com.cdwater.ticketbox.inventory.domain.repository;

import com.cdwater.ticketbox.inventory.domain.model.ShowSeat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ShowSeatInventoryRepository {

    Map<String, Long> countAvailableByZone(Long showId);

    /** 锁定指定区域的可售座位；不足 quantity 时抛出冲突。 */
    List<ShowSeat> lockSeats(Long showId, String zone, int quantity,
                             Long orderNo, LocalDateTime expireTime);

    List<ShowSeat> findLockedByOrder(Long orderNo);

    int confirmByOrder(Long orderNo);

    int releaseByOrder(Long orderNo);
}
