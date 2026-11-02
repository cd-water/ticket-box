package com.cdwater.ticketbox.cinema.application.query.converter;

import com.cdwater.ticketbox.cinema.application.query.view.ScreeningView;
import com.cdwater.ticketbox.cinema.application.query.view.SeatView;
import com.cdwater.ticketbox.cinema.domain.model.ScreeningDetails;
import com.cdwater.ticketbox.inventory.api.dto.MovieSeatDTO;
import com.cdwater.ticketbox.cinema.application.query.view.SeatMapView;
import com.cdwater.ticketbox.cinema.domain.model.Cinema;
import com.cdwater.ticketbox.cinema.domain.model.Hall;
import com.cdwater.ticketbox.cinema.domain.model.Screening;
import com.cdwater.ticketbox.cinema.application.port.MovieInfo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ScreeningViewConverter {

    @Mapping(target = "availableSeats", source = "availableSeats")
    ScreeningView toView(ScreeningDetails details, long availableSeats);

    @Mapping(target = "seatId", source = "id")
    SeatView toView(MovieSeatDTO seat);


    @Mapping(target = "screeningId", source = "screening.id")
    @Mapping(target = "cinemaName", source = "cinema.name")
    @Mapping(target = "movieTitle", source = "movie.title")
    @Mapping(target = "hallName", source = "hall.name")
    @Mapping(target = "startTime", source = "screening.startTime")
    @Mapping(target = "endTime", source = "screening.endTime")
    @Mapping(target = "price", source = "screening.price")
    @Mapping(target = "rowCount", ignore = true)
    @Mapping(target = "colCount", ignore = true)
    @Mapping(target = "seats", ignore = true)
    SeatMapView toView(Screening screening, Cinema cinema, Hall hall, MovieInfo movie);
}
