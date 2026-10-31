package com.cdwater.ticketbox.cinema.application.service;

import com.cdwater.ticketbox.cinema.application.dto.ReservedSeat;
import com.cdwater.ticketbox.cinema.domain.model.SeatSnapshot;
import com.cdwater.ticketbox.cinema.domain.repository.ScreeningSeatRepository;
import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatReservationService {

    private final ScreeningSeatRepository screeningSeatRepository;

    @Transactional
    public void releaseExpiredByScreening(Long screeningId, LocalDateTime now) {
        screeningSeatRepository.releaseExpiredByScreening(screeningId, now);
    }

    @Transactional
    public List<ReservedSeat> lockSeats(Long screeningId, List<Long> rawSeatIds,
                                       Long orderId, LocalDateTime expireTime) {
        List<Long> seatIds = rawSeatIds.stream().sorted().toList();
        List<SeatSnapshot> rows = screeningSeatRepository.lockSeats(screeningId, seatIds);
        if (rows.size() != seatIds.size()) {
            throw new BizException(ResultCode.NOT_FOUND, "座位不存在或不属于该排场");
        }
        LocalDateTime now = LocalDateTime.now();
        for (SeatSnapshot row : rows) {
            boolean available = row.getStatus() == 0
                    || (row.getStatus() == 1
                        && row.getLockExpireTime() != null
                        && row.getLockExpireTime().isBefore(now));
            if (!available) {
                throw new BizException(ResultCode.CONFLICT, "座位已被占用");
            }
        }
        screeningSeatRepository.updateLocks(screeningId, seatIds, orderId, expireTime);
        return rows.stream()
                .map(row -> new ReservedSeat(row.getSeatId(), row.getRowNum(), row.getColNum()))
                .toList();
    }

    @Transactional
    public void releaseByOrder(Long orderId) {
        screeningSeatRepository.releaseByOrder(orderId);
    }
}
