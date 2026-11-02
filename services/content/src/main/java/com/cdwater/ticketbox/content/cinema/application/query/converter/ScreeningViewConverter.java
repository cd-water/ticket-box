package com.cdwater.ticketbox.content.cinema.application.query.converter;

import com.cdwater.ticketbox.content.api.dto.ScreeningDTO;
import com.cdwater.ticketbox.content.api.dto.SeatDTO;
import com.cdwater.ticketbox.content.cinema.domain.model.ScreeningDetails;
import com.cdwater.ticketbox.inventory.api.dto.MovieSeatDTO;
import com.cdwater.ticketbox.content.api.dto.SeatMapDTO;
import com.cdwater.ticketbox.content.cinema.domain.model.Cinema;
import com.cdwater.ticketbox.content.cinema.domain.model.Hall;
import com.cdwater.ticketbox.content.cinema.domain.model.Screening;
import com.cdwater.ticketbox.content.cinema.application.port.MovieInfo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ScreeningViewConverter {

    @Mapping(target = "availableSeats", source = "availableSeats")
    ScreeningDTO toView(ScreeningDetails details, long availableSeats);

    @Mapping(target = "seatId", source = "id")
    SeatDTO toView(MovieSeatDTO seat);


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
    SeatMapDTO toView(Screening screening, Cinema cinema, Hall hall, MovieInfo movie);
}