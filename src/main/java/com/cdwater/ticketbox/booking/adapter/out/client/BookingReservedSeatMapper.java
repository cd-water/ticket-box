package com.cdwater.ticketbox.booking.adapter.out.client;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingReservedSeatMapper {

    com.cdwater.ticketbox.booking.application.port.ReservedSeat toSeat(
            com.cdwater.ticketbox.cinema.application.command.ReservedSeat seat);
}
