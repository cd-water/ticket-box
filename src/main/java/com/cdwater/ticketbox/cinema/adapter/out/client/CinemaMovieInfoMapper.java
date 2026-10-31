package com.cdwater.ticketbox.cinema.adapter.out.client;

import com.cdwater.ticketbox.cinema.application.port.MovieInfo;
import com.cdwater.ticketbox.movie.application.query.model.MovieBrief;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CinemaMovieInfoMapper {

    MovieInfo toInfo(MovieBrief movie);
}
