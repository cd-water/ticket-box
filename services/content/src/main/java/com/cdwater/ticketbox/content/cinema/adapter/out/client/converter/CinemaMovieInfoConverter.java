package com.cdwater.ticketbox.content.cinema.adapter.out.client.converter;

import com.cdwater.ticketbox.content.cinema.application.port.MovieInfo;
import com.cdwater.ticketbox.content.api.dto.MovieBriefDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CinemaMovieInfoConverter {

    MovieInfo toInfo(MovieBriefDTO movie);
}