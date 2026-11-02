package com.cdwater.ticketbox.content.movie.adapter.out.persistence.converter;

import com.cdwater.ticketbox.content.movie.adapter.out.persistence.projection.MoviePersonRow;
import com.cdwater.ticketbox.content.movie.domain.model.MoviePerson;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MovieProjectionConverter {

    MoviePerson toDomain(MoviePersonRow row);
}
