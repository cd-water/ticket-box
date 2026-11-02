package com.cdwater.ticketbox.content.cinema.application.query.converter;

import com.cdwater.ticketbox.content.cinema.application.port.MovieInfo;
import com.cdwater.ticketbox.content.api.dto.CinemaListItemDTO;
import com.cdwater.ticketbox.content.api.dto.CinemaMovieDTO;
import com.cdwater.ticketbox.content.cinema.domain.model.Cinema;
import com.cdwater.ticketbox.content.api.dto.CinemaDetailDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CinemaViewConverter {

    CinemaListItemDTO toView(Cinema cinema);

    CinemaMovieDTO toView(MovieInfo movie);


    @Mapping(target = "movies", ignore = true)
    CinemaDetailDTO toDetailView(Cinema cinema);
}