package com.cdwater.ticketbox.movie.adapter.out.persistence;

import com.cdwater.ticketbox.movie.adapter.out.persistence.projection.MoviePersonRow;
import com.cdwater.ticketbox.movie.domain.model.MoviePerson;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MovieProjectionMapper {

    MoviePerson toDomain(MoviePersonRow row);
}
