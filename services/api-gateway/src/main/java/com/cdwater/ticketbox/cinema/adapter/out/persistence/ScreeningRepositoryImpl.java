package com.cdwater.ticketbox.cinema.adapter.out.persistence;

import com.cdwater.ticketbox.cinema.adapter.out.persistence.mapper.ScreeningMapper;
import com.cdwater.ticketbox.cinema.adapter.out.persistence.converter.ScreeningProjectionConverter;
import com.cdwater.ticketbox.cinema.adapter.out.persistence.projection.SeatInventoryCountRow;
import com.cdwater.ticketbox.cinema.adapter.out.persistence.converter.ScreeningConverter;
import com.cdwater.ticketbox.cinema.adapter.out.persistence.po.ScreeningPO;
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
    private final ScreeningProjectionConverter screeningProjectionConverter;
    private final ScreeningConverter screeningConverter;

    @Override
    public List<Long> findFutureMovieIds(Long cinemaId) {
        return screeningMapper.selectFutureMovieIds(cinemaId);
    }

    @Override
    public List<ScreeningDetails> findFutureWithHall(Long cinemaId, Long movieId) {
        return screeningMapper.selectFutureWithHall(cinemaId, movieId).stream()
                .map(screeningProjectionConverter::toDomain)
                .toList();
    }

    @Override
    public Optional<Screening> findById(Long id) {
        return Optional.ofNullable(screeningMapper.selectById(id)).map(screeningConverter::toDomain);
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
