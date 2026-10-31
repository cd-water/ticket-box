package com.cdwater.ticketbox.movie.adapter.out.persistence;

import com.cdwater.ticketbox.movie.adapter.out.persistence.projection.MovieCrewRow;
import com.cdwater.ticketbox.movie.domain.model.MovieCrewMember;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MovieProjectionMapper {

    MovieCrewMember toDomain(MovieCrewRow row);
}
