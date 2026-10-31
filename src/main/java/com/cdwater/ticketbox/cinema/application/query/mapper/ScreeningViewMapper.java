package com.cdwater.ticketbox.cinema.application.query.mapper;

import com.cdwater.ticketbox.cinema.application.query.view.ScreeningView;
import com.cdwater.ticketbox.cinema.application.query.view.SeatView;
import com.cdwater.ticketbox.cinema.domain.model.ScreeningDetails;
import com.cdwater.ticketbox.cinema.domain.model.SeatSnapshot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScreeningViewMapper {

    @Mapping(target = "availableSeats", source = "availableSeats")
    ScreeningView toView(ScreeningDetails details, long availableSeats);

    SeatView toView(SeatSnapshot seat);
}
