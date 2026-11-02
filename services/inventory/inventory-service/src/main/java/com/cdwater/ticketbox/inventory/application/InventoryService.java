package com.cdwater.ticketbox.inventory.application;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.ResultCode;
import com.cdwater.ticketbox.inventory.application.command.SeatPicker;
import com.cdwater.ticketbox.inventory.domain.model.SeatSnapshot;
import com.cdwater.ticketbox.inventory.domain.model.ShowSeat;
import com.cdwater.ticketbox.inventory.domain.repository.PlayStockRepository;
import com.cdwater.ticketbox.inventory.domain.repository.SeatInventoryRepository;
import com.cdwater.ticketbox.inventory.domain.repository.ShowSeatInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final SeatInventoryRepository movieSeats;
    private final ShowSeatInventoryRepository showSeats;
    private final PlayStockRepository playStock;

    @Transactional
    public List<SeatSnapshot> seatMap(Long screeningId) {
        movieSeats.releaseExpiredByScreening(screeningId, LocalDateTime.now());
        return movieSeats.findSeatMap(screeningId);
    }

    @Transactional
    public void releaseExpired(Long screeningId) {
        movieSeats.releaseExpiredByScreening(screeningId, LocalDateTime.now());
    }

    @Transactional
    public List<SeatSnapshot> reserve(Long screeningId, List<Long> rawSeatIds,
                                      Long orderNo, LocalDateTime expireTime) {
        if (rawSeatIds == null || rawSeatIds.isEmpty()) {
            throw new BizException(ResultCode.BAD_REQUEST, "请选择有效座位");
        }
        List<Long> seatIds = rawSeatIds.stream().sorted().toList();
        List<SeatSnapshot> rows = movieSeats.lockSeats(screeningId, seatIds);
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
        movieSeats.updateLocks(screeningId, seatIds, orderNo, expireTime);
        return rows;
    }

    @Transactional
    public int confirm(Long orderNo) {
        return movieSeats.markSoldByOrder(orderNo);
    }

    @Transactional
    public int rollbackMovieSale(Long orderNo) {
        return movieSeats.rollbackSale(orderNo);
    }

    @Transactional
    public void release(Long orderNo) {
        movieSeats.releaseByOrder(orderNo);
    }

    public Map<Long, Long> countMovieSeats(Collection<Long> screeningIds) {
        return movieSeats.countAvailable(screeningIds);
    }

    public Map<String, Long> countShowSeats(Long showId) {
        return showSeats.countAvailableByZone(showId);
    }

    @Transactional
    public List<ShowSeat> sellShowSeats(Long showId, String zone, int quantity, Long orderNo) {
        List<ShowSeat> alreadySold = showSeats.findSoldByOrder(orderNo);
        if (!alreadySold.isEmpty()) {
            return alreadySold;
        }
        List<ShowSeat> seats = showSeats.lockAvailableSeats(showId, zone);
        if (seats.size() < quantity) {
            throw new BizException(ResultCode.CONFLICT, "该区域余票不足");
        }
        List<ShowSeat> picked = SeatPicker.pick(seats, quantity);
        showSeats.markSold(picked.stream().map(ShowSeat::getId).toList(), orderNo);
        return picked;
    }

    @Transactional
    public int rollbackShowSale(Long orderNo) {
        return showSeats.rollbackSale(orderNo);
    }

    @Transactional
    public boolean deductPlayStock(Long itemId, int quantity) {
        if (quantity <= 0) {
            throw new BizException(ResultCode.BAD_REQUEST, "数量必须大于 0");
        }
        return playStock.deduct(itemId, quantity);
    }

    @Transactional
    public int restorePlayStock(Long itemId, int quantity) {
        return playStock.restore(itemId, quantity);
    }

    public Map<Long, Integer> playStocks(Collection<Long> itemIds) {
        return playStock.getStocks(itemIds);
    }
}
