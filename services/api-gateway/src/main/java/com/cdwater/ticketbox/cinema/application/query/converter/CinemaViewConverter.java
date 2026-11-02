package com.cdwater.ticketbox.cinema.application.query.converter;

import com.cdwater.ticketbox.cinema.application.port.MovieInfo;
import com.cdwater.ticketbox.cinema.application.query.view.CinemaListItemView;
import com.cdwater.ticketbox.cinema.application.query.view.CinemaMovieView;
import com.cdwater.ticketbox.cinema.domain.model.Cinema;
import com.cdwater.ticketbox.cinema.application.query.view.CinemaDetailView;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CinemaViewConverter {

    CinemaListItemView toView(Cinema cinema);

    CinemaMovieView toView(MovieInfo movie);


    @Mapping(target = "movies", ignore = true)
    CinemaDetailView toDetailView(Cinema cinema);
}
