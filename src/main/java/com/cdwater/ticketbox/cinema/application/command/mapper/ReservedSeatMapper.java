package com.cdwater.ticketbox.cinema.application.command.mapper;

import com.cdwater.ticketbox.cinema.application.command.ReservedSeat;
import com.cdwater.ticketbox.cinema.domain.model.SeatSnapshot;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReservedSeatMapper {

    ReservedSeat toSeat(SeatSnapshot snapshot);
}
