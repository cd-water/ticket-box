package com.cdwater.ticketbox.cinema.application.command;

import com.cdwater.ticketbox.cinema.application.command.mapper.ReservedSeatMapper;
import com.cdwater.ticketbox.cinema.application.command.ReservedSeat;
import com.cdwater.ticketbox.cinema.domain.model.SeatSnapshot;
import com.cdwater.ticketbox.cinema.domain.repository.SeatInventoryRepository;
import com.cdwater.ticketbox.shared.error.BizException;
import com.cdwater.ticketbox.shared.error.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatInventoryService {

    private final SeatInventoryRepository seatInventoryRepository;
    private final ReservedSeatMapper reservedSeatMapper;

    @Transactional
    public void releaseExpiredByScreening(Long screeningId, LocalDateTime now) {
        seatInventoryRepository.releaseExpiredByScreening(screeningId, now);
    }

    @Transactional
    public List<ReservedSeat> lockSeats(Long screeningId, List<Long> rawSeatIds,
                                       Long orderId, LocalDateTime expireTime) {
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
        seatInventoryRepository.updateLocks(screeningId, seatIds, orderId, expireTime);
        return rows.stream()
                .map(reservedSeatMapper::toSeat)
                .toList();
    }

    @Transactional
    public void releaseByOrder(Long orderId) {
        seatInventoryRepository.releaseByOrder(orderId);
    }
}
