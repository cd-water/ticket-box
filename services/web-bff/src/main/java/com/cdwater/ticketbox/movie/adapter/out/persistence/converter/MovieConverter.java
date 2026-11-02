package com.cdwater.ticketbox.movie.adapter.out.persistence.converter;

import com.cdwater.ticketbox.movie.adapter.out.persistence.po.MoviePO;
import com.cdwater.ticketbox.movie.domain.model.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MovieConverter {

    Movie toDomain(MoviePO po);

    List<Movie> toDomain(List<MoviePO> poList);

    MoviePO toPO(Movie domain);
}
