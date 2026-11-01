package com.cdwater.ticketbox.booking.application.port;

import java.time.LocalDateTime;
import java.util.List;

public interface SeatInventoryPort {

    List<ReservedSeat> lockSeats(Long screeningId, List<Long> seatIds,
                                 Long orderNo, LocalDateTime expireTime);

    void releaseByOrder(Long orderNo);
}
