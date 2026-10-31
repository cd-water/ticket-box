package com.cdwater.ticketbox.booking.application.query.mapper;

import com.cdwater.ticketbox.booking.application.query.view.OrderSeatView;
import com.cdwater.ticketbox.booking.domain.model.OrderSeat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderSeatViewMapper {

    OrderSeatView toView(OrderSeat seat);
}
