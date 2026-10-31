package com.cdwater.ticketbox.movie.domain.repository;

import com.cdwater.ticketbox.shared.query.PageResult;
import com.cdwater.ticketbox.movie.domain.model.Genre;
import com.cdwater.ticketbox.movie.domain.model.Movie;
import com.cdwater.ticketbox.movie.domain.model.MovieCrewMember;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MovieRepository {

    PageResult<Movie> page(String tab, String keyword, long page, long size);

    Optional<Movie> findById(Long id);

    Map<Long, Movie> findByIds(Collection<Long> ids);

    List<Genre> findGenres(Long movieId);

    List<MovieCrewMember> findCrew(Long movieId);
}
