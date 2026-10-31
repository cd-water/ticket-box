package com.cdwater.ticketbox.booking.adapter.out.client;

import com.cdwater.ticketbox.booking.application.port.MovieInfo;
import com.cdwater.ticketbox.movie.application.query.model.MovieBrief;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingMovieInfoMapper {

    MovieInfo toInfo(MovieBrief movie);
}
