package com.cdwater.ticketbox.show.domain.repository;

import com.cdwater.ticketbox.show.domain.model.ShowSeat;

import java.util.Collection;
import java.util.List;

public interface ShowSeatInventoryRepository {

    List<ShowSeat> lockAvailableSeats(Long showId, String zone);

    int markSold(Collection<Long> seatIds, Long orderNo);
}
