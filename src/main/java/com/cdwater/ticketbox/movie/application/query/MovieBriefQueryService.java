package com.cdwater.ticketbox.movie.application.query;

import com.cdwater.ticketbox.movie.application.query.mapper.MovieViewMapper;
import com.cdwater.ticketbox.movie.application.query.model.MovieBrief;
import com.cdwater.ticketbox.movie.domain.model.Movie;
import com.cdwater.ticketbox.movie.domain.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieBriefQueryService {

    private final MovieRepository movieRepository;
    private final MovieViewMapper movieViewMapper;

    public Map<Long, MovieBrief> findByIds(Collection<Long> ids) {
        return movieRepository.findByIds(ids).values().stream()
                .collect(Collectors.toMap(Movie::getId, movieViewMapper::toBrief));
    }

    public Optional<MovieBrief> findById(Long id) {
        return movieRepository.findById(id).map(movieViewMapper::toBrief);
    }
}
