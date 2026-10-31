package com.cdwater.ticketbox.cinema.infrastructure.persistence;

import com.cdwater.ticketbox.cinema.domain.model.Screening;
import com.cdwater.ticketbox.cinema.domain.repository.ScreeningRepository;
import com.cdwater.ticketbox.cinema.infrastructure.persistence.mapper.ScreeningMapper;
import com.cdwater.ticketbox.cinema.infrastructure.persistence.projection.ScreeningRow;
import com.cdwater.ticketbox.cinema.infrastructure.persistence.projection.ScreeningSeatCountRow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ScreeningRepositoryImpl implements ScreeningRepository {

    private final ScreeningMapper screeningMapper;

    @Override
    public List<Long> findFutureMovieIds(Long cinemaId) {
        return screeningMapper.selectFutureMovieIds(cinemaId);
    }

    @Override
    public List<ScreeningRow> findFutureWithHall(Long cinemaId, Long movieId) {
        return screeningMapper.selectFutureWithHall(cinemaId, movieId);
    }

    @Override
    public Optional<Screening> findById(Long id) {
        return Optional.ofNullable(screeningMapper.selectById(id));
    }

    @Override
    public Map<Long, Long> countAvailableSeats(Collection<Long> screeningIds) {
        if (screeningIds == null || screeningIds.isEmpty()) {
            return Map.of();
        }
        return screeningMapper.selectAvailableCounts(screeningIds).stream()
                .collect(Collectors.toMap(ScreeningSeatCountRow::getScreeningId,
                        ScreeningSeatCountRow::getAvailableCount));
    }
}
