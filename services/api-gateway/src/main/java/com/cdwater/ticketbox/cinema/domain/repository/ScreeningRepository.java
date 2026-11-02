package com.cdwater.ticketbox.cinema.domain.repository;

import com.cdwater.ticketbox.cinema.domain.model.Screening;
import com.cdwater.ticketbox.cinema.domain.model.ScreeningDetails;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ScreeningRepository {

    List<Long> findFutureMovieIds(Long cinemaId);

    List<ScreeningDetails> findFutureWithHall(Long cinemaId, Long movieId);

    Optional<Screening> findById(Long id);

    Map<Long, Long> countAvailableSeats(Collection<Long> screeningIds);
}
