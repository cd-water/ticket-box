package com.cdwater.ticketbox.home.adapter.out.client.converter;

import com.cdwater.ticketbox.home.application.query.view.HomeMovieItem;
import com.cdwater.ticketbox.movie.application.query.view.MovieListItemView;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MovieHomeConverter {

    HomeMovieItem toItem(MovieListItemView view);
}
