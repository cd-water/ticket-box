package com.cdwater.ticketbox.inventory.adapter.in.rpc;

import com.cdwater.ticketbox.inventory.api.MovieSeatInventoryApi;
import com.cdwater.ticketbox.inventory.api.dto.MovieSeatDTO;
import com.cdwater.ticketbox.inventory.api.dto.MovieSeatSnapshotDTO;
import com.cdwater.ticketbox.inventory.application.InventoryService;
import com.cdwater.ticketbox.inventory.domain.model.SeatSnapshot;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@DubboService
@RequiredArgsConstructor
public class MovieSeatInventoryApiImpl implements MovieSeatInventoryApi {

    private final InventoryService inventoryService;

    @Override
    public List<MovieSeatDTO> seatMap(long screeningId) {
        return inventoryService.seatMap(screeningId).stream().map(this::toSeat).toList();
    }

    @Override
    public Map<Long, Long> countAvailable(Collection<Long> screeningIds) {
        return inventoryService.countMovieSeats(screeningIds);
    }

    @Override
    public List<MovieSeatSnapshotDTO> reserve(long screeningId, List<Long> seatIds,
                                              long orderNo, LocalDateTime expireAt) {
        return inventoryService.reserve(screeningId, seatIds, orderNo, expireAt)
                .stream().map(this::toSnapshot).toList();
    }

    @Override
    public int confirm(long orderNo) {
        return inventoryService.confirm(orderNo);
    }

    @Override
    public int rollbackSale(long orderNo) {
        return inventoryService.rollbackMovieSale(orderNo);
    }

    @Override
    public void release(long orderNo) {
        inventoryService.release(orderNo);
    }

    @Override
    public void releaseExpired(long screeningId) {
        inventoryService.releaseExpired(screeningId);
    }

    private MovieSeatDTO toSeat(SeatSnapshot seat) {
        MovieSeatDTO dto = new MovieSeatDTO();
        dto.setId(seat.getSeatId());
        dto.setRowNum(seat.getRowNum());
        dto.setColNum(seat.getColNum());
        dto.setStatus(seat.getStatus());
        return dto;
    }

    private MovieSeatSnapshotDTO toSnapshot(SeatSnapshot seat) {
        MovieSeatSnapshotDTO dto = new MovieSeatSnapshotDTO();
        dto.setId(seat.getSeatId());
        dto.setRowNum(seat.getRowNum());
        dto.setColNum(seat.getColNum());
        dto.setStatus(seat.getStatus());
        return dto;
    }
}
