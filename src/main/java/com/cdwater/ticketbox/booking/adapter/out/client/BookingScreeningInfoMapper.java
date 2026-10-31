package com.cdwater.ticketbox.booking.adapter.out.client;

import com.cdwater.ticketbox.booking.application.port.ScreeningInfo;
import com.cdwater.ticketbox.cinema.application.query.ScreeningBookingInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingScreeningInfoMapper {

    ScreeningInfo toInfo(ScreeningBookingInfo info);
}
