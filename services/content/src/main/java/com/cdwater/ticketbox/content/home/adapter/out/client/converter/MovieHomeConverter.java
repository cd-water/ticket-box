package com.cdwater.ticketbox.content.home.adapter.out.client.converter;

import com.cdwater.ticketbox.content.api.dto.HomeMovieDTO;
import com.cdwater.ticketbox.content.api.dto.MovieListItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MovieHomeConverter {

    HomeMovieDTO toItem(MovieListItemDTO view);
}