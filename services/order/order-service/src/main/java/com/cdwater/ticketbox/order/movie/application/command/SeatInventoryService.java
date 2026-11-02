package com.cdwater.ticketbox.order.movie.application.command;

import com.cdwater.ticketbox.inventory.api.MovieSeatInventoryApi;
import com.cdwater.ticketbox.inventory.api.dto.MovieSeatSnapshotDTO;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatInventoryService {

    @DubboReference(check = false, timeout = 3000)
    private MovieSeatInventoryApi movieSeatInventoryApi;

    public void releaseExpiredByScreening(Long screeningId) {
        movieSeatInventoryApi.releaseExpired(screeningId);
    }

    public List<MovieSeatSnapshotDTO> lockSeats(Long screeningId, List<Long> seatIds,
                                                Long orderNo, LocalDateTime expireTime) {
        return movieSeatInventoryApi.reserve(screeningId, seatIds, orderNo, expireTime);
    }

    public void releaseByOrder(Long orderNo) {
        movieSeatInventoryApi.release(orderNo);
    }

    public int markSoldByOrder(Long orderNo) {
        return movieSeatInventoryApi.confirm(orderNo);
    }

    public int rollbackSale(Long orderNo) {
        return movieSeatInventoryApi.rollbackSale(orderNo);
    }
}
