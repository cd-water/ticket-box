package com.cdwater.ticketbox.inventory.adapter.in.rpc;

import com.cdwater.ticketbox.inventory.api.InventoryApi;
import com.cdwater.ticketbox.inventory.api.dto.HoldCommandDTO;
import com.cdwater.ticketbox.inventory.api.dto.HoldDTO;
import com.cdwater.ticketbox.inventory.api.dto.MovieSeatDTO;
import com.cdwater.ticketbox.inventory.application.InventoryService;
import com.cdwater.ticketbox.inventory.domain.model.MovieSeatSnapshot;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@DubboService
@RequiredArgsConstructor
public class InventoryApiImpl implements InventoryApi {

    private final InventoryService inventoryService;

    @Override
    public List<MovieSeatDTO> getSeatMap(long screeningId) {
        return inventoryService.getSeatMap(screeningId).stream().map(this::toSeat).toList();
    }

    @Override
    public Map<Long, Long> countAvailableSeats(Collection<Long> screeningIds) {
        return inventoryService.countAvailableMovieSeats(screeningIds);
    }

    @Override
    public Map<String, Long> countAvailableZones(long showId) {
        return inventoryService.countAvailableShowSeats(showId);
    }

    @Override
    public Map<Long, Integer> getPlayStocks(Collection<Long> itemIds) {
        return inventoryService.getPlayStocks(itemIds);
    }

    @Override
    public HoldDTO hold(HoldCommandDTO command) {
        return inventoryService.hold(command);
    }

    @Override
    public void confirm(long orderNo) {
        inventoryService.confirm(orderNo);
    }

    @Override
    public void release(long orderNo) {
        inventoryService.release(orderNo);
    }

    private MovieSeatDTO toSeat(MovieSeatSnapshot seat) {
        MovieSeatDTO dto = new MovieSeatDTO();
        dto.setId(seat.getSeatId());
        dto.setRowNum(seat.getRowNum());
        dto.setColNum(seat.getColNum());
        dto.setStatus(seat.getStatus());
        return dto;
    }
}
