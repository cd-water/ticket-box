package com.cdwater.ticketbox.movie.application.query;

import com.cdwater.ticketbox.movie.domain.model.Movie;
import com.cdwater.ticketbox.movie.infrastructure.persistence.mapper.MovieMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieQueryService {

    private final MovieMapper movieMapper;

    public Map<Long, MovieBrief> findByIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Map.of();
        }
        return movieMapper.selectByIds(ids).stream()
                .collect(Collectors.toMap(Movie::getId, this::toBrief));
    }

    public Optional<MovieBrief> findById(Long id) {
        return Optional.ofNullable(movieMapper.selectById(id)).map(this::toBrief);
    }

    private MovieBrief toBrief(Movie movie) {
        return new MovieBrief(movie.getId(), movie.getTitle(), movie.getPoster(),
                movie.getReleaseDate(), movie.getDuration(), movie.getStatus());
    }
}
