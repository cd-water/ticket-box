package com.cdwater.ticketbox.content.movie.application.query;

import com.cdwater.ticketbox.content.movie.application.query.converter.MovieViewConverter;
import com.cdwater.ticketbox.content.api.dto.MovieBriefDTO;
import com.cdwater.ticketbox.content.movie.domain.model.Movie;
import com.cdwater.ticketbox.content.movie.domain.repository.MovieRepository;
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
    private final MovieViewConverter movieViewMapper;

    public Map<Long, MovieBriefDTO> findByIds(Collection<Long> ids) {
        return movieRepository.findByIds(ids).values().stream()
                .collect(Collectors.toMap(Movie::getId, movieViewMapper::toBrief));
    }

    public Optional<MovieBriefDTO> findById(Long id) {
        return movieRepository.findById(id).map(movieViewMapper::toBrief);
    }
}