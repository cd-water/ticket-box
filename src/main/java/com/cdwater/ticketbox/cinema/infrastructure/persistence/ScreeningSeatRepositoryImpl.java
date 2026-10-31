package com.cdwater.ticketbox.cinema.infrastructure.persistence;

import com.cdwater.ticketbox.cinema.domain.model.SeatSnapshot;
import com.cdwater.ticketbox.cinema.domain.repository.ScreeningSeatRepository;
import com.cdwater.ticketbox.cinema.infrastructure.persistence.mapper.ScreeningSeatMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ScreeningSeatRepositoryImpl implements ScreeningSeatRepository {

    private final ScreeningSeatMapper screeningSeatMapper;

    @Override
    public List<SeatSnapshot> findSeatMap(Long screeningId) {
        return screeningSeatMapper.selectSeatMap(screeningId);
    }

    @Override
    public List<SeatSnapshot> lockSeats(Long screeningId, Collection<Long> seatIds) {
        return screeningSeatMapper.selectSeatsForUpdate(screeningId, seatIds);
    }

    @Override
    public int updateLocks(Long screeningId, Collection<Long> seatIds,
                           Long orderId, LocalDateTime expireTime) {
        return screeningSeatMapper.updateLocks(screeningId, seatIds, orderId, expireTime);
    }

    @Override
    public int releaseByOrder(Long orderId) {
        return screeningSeatMapper.releaseByOrder(orderId);
    }

    @Override
    public int releaseExpiredByScreening(Long screeningId, LocalDateTime now) {
        return screeningSeatMapper.releaseExpiredByScreening(screeningId, now);
    }
}
