package com.cdwater.ticketbox.cinema.adapter.out.persistence;

import com.cdwater.ticketbox.cinema.domain.model.SeatSnapshot;
import com.cdwater.ticketbox.cinema.domain.repository.SeatInventoryRepository;
import com.cdwater.ticketbox.cinema.adapter.out.persistence.mapper.SeatInventoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SeatInventoryRepositoryImpl implements SeatInventoryRepository {

    private final SeatInventoryMapper seatInventoryMapper;

    @Override
    public List<SeatSnapshot> findSeatMap(Long screeningId) {
        return seatInventoryMapper.selectSeatMap(screeningId);
    }

    @Override
    public List<SeatSnapshot> lockSeats(Long screeningId, Collection<Long> seatIds) {
        return seatInventoryMapper.selectSeatsForUpdate(screeningId, seatIds);
    }

    @Override
    public int updateLocks(Long screeningId, Collection<Long> seatIds,
                           Long orderNo, LocalDateTime expireTime) {
        return seatInventoryMapper.updateLocks(screeningId, seatIds, orderNo, expireTime);
    }

    @Override
    public int releaseByOrder(Long orderNo) {
        return seatInventoryMapper.releaseByOrder(orderNo);
    }

    @Override
    public int markSoldByOrder(Long orderNo) {
        return seatInventoryMapper.markSoldByOrder(orderNo);
    }

    @Override
    public int releaseExpiredByScreening(Long screeningId, LocalDateTime now) {
        return seatInventoryMapper.releaseExpiredByScreening(screeningId, now);
    }
}
