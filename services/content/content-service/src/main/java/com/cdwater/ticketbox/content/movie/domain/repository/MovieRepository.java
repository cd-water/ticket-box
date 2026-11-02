package com.cdwater.ticketbox.content.movie.domain.repository;

import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.content.movie.domain.model.Genre;
import com.cdwater.ticketbox.content.movie.domain.model.Movie;
import com.cdwater.ticketbox.content.movie.domain.model.MoviePerson;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MovieRepository {

    PageResult<Movie> page(String tab, String keyword, long page, long size);

    Optional<Movie> findById(Long id);

    Map<Long, Movie> findByIds(Collection<Long> ids);

    List<Genre> findGenres(Long movieId);

    List<MoviePerson> findPersons(Long movieId);
}
