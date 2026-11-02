package com.cdwater.ticketbox.cinema.adapter.out.client.converter;

import com.cdwater.ticketbox.cinema.application.port.MovieInfo;
import com.cdwater.ticketbox.movie.application.query.model.MovieBrief;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CinemaMovieInfoConverter {

    MovieInfo toInfo(MovieBrief movie);
}
