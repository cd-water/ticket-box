package com.cdwater.ticketbox.booking.application.command.mapper;

import com.cdwater.ticketbox.booking.application.port.ReservedSeat;
import com.cdwater.ticketbox.booking.domain.model.OrderSeat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderSeatDomainMapper {

    OrderSeat toSeat(ReservedSeat seat);
}
