package com.cdwater.ticketbox.booking.adapter.out.client;

import com.cdwater.ticketbox.booking.application.port.ScreeningInfo;
import com.cdwater.ticketbox.booking.application.port.ScreeningInfoPort;
import com.cdwater.ticketbox.cinema.application.query.ScreeningBookingInfo;
import com.cdwater.ticketbox.cinema.application.query.ScreeningQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CinemaScreeningAdapter implements ScreeningInfoPort {

    private final ScreeningQueryService screeningQueryService;
    private final BookingScreeningInfoMapper bookingScreeningInfoMapper;

    @Override
    public ScreeningInfo get(Long screeningId) {
        ScreeningBookingInfo info = screeningQueryService.getBookingInfo(screeningId);
        return bookingScreeningInfoMapper.toInfo(info);
    }
}
