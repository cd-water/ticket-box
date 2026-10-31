package com.cdwater.ticketbox.booking.adapter.out.client;

import com.cdwater.ticketbox.booking.application.port.MovieInfo;
import com.cdwater.ticketbox.booking.application.port.MovieInfoPort;
import com.cdwater.ticketbox.movie.application.query.MovieBriefQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MovieInfoAdapter implements MovieInfoPort {

    private final MovieBriefQueryService movieBriefQueryService;
    private final BookingMovieInfoMapper bookingMovieInfoMapper;

    @Override
    public Optional<MovieInfo> findById(Long id) {
        return movieBriefQueryService.findById(id).map(bookingMovieInfoMapper::toInfo);
    }
}
