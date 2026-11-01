package com.cdwater.ticketbox.booking.adapter.out.client;

import com.cdwater.ticketbox.booking.application.port.ReservedSeat;
import com.cdwater.ticketbox.booking.application.port.SeatInventoryPort;
import com.cdwater.ticketbox.cinema.application.command.SeatInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CinemaSeatInventoryAdapter implements SeatInventoryPort {

    private final SeatInventoryService seatInventoryService;
    private final BookingReservedSeatMapper bookingReservedSeatMapper;

    @Override
    public List<ReservedSeat> lockSeats(Long screeningId, List<Long> seatIds,
                                        Long orderNo, LocalDateTime expireTime) {
        return seatInventoryService.lockSeats(screeningId, seatIds, orderNo, expireTime).stream()
                .map(bookingReservedSeatMapper::toSeat)
                .toList();
    }

    @Override
    public void releaseByOrder(Long orderNo) {
        seatInventoryService.releaseByOrder(orderNo);
    }
}
