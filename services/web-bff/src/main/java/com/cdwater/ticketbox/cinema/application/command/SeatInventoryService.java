package com.cdwater.ticketbox.cinema.application.command;

import com.cdwater.ticketbox.cinema.domain.model.SeatSnapshot;
import com.cdwater.ticketbox.cinema.domain.repository.SeatInventoryRepository;
import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatInventoryService {

    private final SeatInventoryRepository seatInventoryRepository;

    @Transactional
    public void releaseExpiredByScreening(Long screeningId, LocalDateTime now) {
        seatInventoryRepository.releaseExpiredByScreening(screeningId, now);
    }

    @Transactional
    public List<SeatSnapshot> lockSeats(Long screeningId, List<Long> rawSeatIds,
                                       Long orderNo, LocalDateTime expireTime) {
        List<Long> seatIds = rawSeatIds.stream().sorted().toList();
        List<SeatSnapshot> rows = seatInventoryRepository.lockSeats(screeningId, seatIds);
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
        seatInventoryRepository.updateLocks(screeningId, seatIds, orderNo, expireTime);
        return rows;
    }

    @Transactional
    public void releaseByOrder(Long orderNo) {
        seatInventoryRepository.releaseByOrder(orderNo);
    }

    @Transactional
    public int markSoldByOrder(Long orderNo) {
        return seatInventoryRepository.markSoldByOrder(orderNo);
    }
}
