package com.cdwater.ticketbox.cinema.adapter.out.persistence;

import com.cdwater.ticketbox.cinema.adapter.out.persistence.mapper.ScreeningMapper;
import com.cdwater.ticketbox.cinema.adapter.out.persistence.mapper.ScreeningProjectionMapper;
import com.cdwater.ticketbox.cinema.adapter.out.persistence.projection.SeatInventoryCountRow;
import com.cdwater.ticketbox.cinema.domain.model.Screening;
import com.cdwater.ticketbox.cinema.domain.model.ScreeningDetails;
import com.cdwater.ticketbox.cinema.domain.repository.ScreeningRepository;
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
    private final ScreeningProjectionMapper screeningProjectionMapper;

    @Override
    public List<Long> findFutureMovieIds(Long cinemaId) {
        return screeningMapper.selectFutureMovieIds(cinemaId);
    }

    @Override
    public List<ScreeningDetails> findFutureWithHall(Long cinemaId, Long movieId) {
        return screeningMapper.selectFutureWithHall(cinemaId, movieId).stream()
                .map(screeningProjectionMapper::toDomain)
                .toList();
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
                .collect(Collectors.toMap(SeatInventoryCountRow::getScreeningId,
                        SeatInventoryCountRow::getAvailableCount));
    }
}
