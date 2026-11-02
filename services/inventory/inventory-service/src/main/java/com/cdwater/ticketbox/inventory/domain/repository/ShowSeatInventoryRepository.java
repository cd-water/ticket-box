package com.cdwater.ticketbox.inventory.domain.repository;

import com.cdwater.ticketbox.inventory.domain.model.ShowSeat;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ShowSeatInventoryRepository {

    Map<String, Long> countAvailableByZone(Long showId);

    List<ShowSeat> lockAvailableSeats(Long showId, String zone);

    List<ShowSeat> findSoldByOrder(Long orderNo);

    int markSold(Collection<Long> seatIds, Long orderNo);

    int rollbackSale(Long orderNo);
}
