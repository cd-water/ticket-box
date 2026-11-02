package com.cdwater.ticketbox.content.cinema.adapter.out.client;

import com.cdwater.ticketbox.content.cinema.adapter.out.client.converter.CinemaMovieInfoConverter;
import com.cdwater.ticketbox.content.cinema.application.port.MovieInfo;
import com.cdwater.ticketbox.content.cinema.application.port.MovieInfoPort;
import com.cdwater.ticketbox.content.movie.application.query.MovieBriefQueryService;
import com.cdwater.ticketbox.content.api.dto.MovieBriefDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CinemaMovieInfoAdapter implements MovieInfoPort {

    private final MovieBriefQueryService movieBriefQueryService;
    private final CinemaMovieInfoConverter cinemaMovieInfoMapper;

    @Override
    public Optional<MovieInfo> findById(Long id) {
        return movieBriefQueryService.findById(id).map(cinemaMovieInfoMapper::toInfo);
    }

    @Override
    public Map<Long, MovieInfo> findByIds(Collection<Long> ids) {
        return movieBriefQueryService.findByIds(ids).values().stream()
                .collect(Collectors.toMap(MovieBriefDTO::getId, cinemaMovieInfoMapper::toInfo));
    }
}