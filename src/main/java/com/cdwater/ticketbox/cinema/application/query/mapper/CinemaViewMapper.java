package com.cdwater.ticketbox.cinema.application.query.mapper;

import com.cdwater.ticketbox.cinema.application.port.MovieInfo;
import com.cdwater.ticketbox.cinema.application.query.view.CinemaListItemView;
import com.cdwater.ticketbox.cinema.application.query.view.CinemaMovieView;
import com.cdwater.ticketbox.cinema.domain.model.Cinema;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CinemaViewMapper {

    CinemaListItemView toView(Cinema cinema);

    CinemaMovieView toView(MovieInfo movie);
}
