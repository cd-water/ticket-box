package com.cdwater.ticketbox.inventory.adapter.in.rpc;

import com.cdwater.ticketbox.inventory.api.ShowSeatInventoryApi;
import com.cdwater.ticketbox.inventory.api.dto.ShowSeatSnapshotDTO;
import com.cdwater.ticketbox.inventory.application.InventoryService;
import com.cdwater.ticketbox.inventory.domain.model.ShowSeat;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;
import java.util.Map;

@DubboService
@RequiredArgsConstructor
public class ShowSeatInventoryApiImpl implements ShowSeatInventoryApi {

    private final InventoryService inventoryService;

    @Override
    public Map<String, Long> countAvailable(long showId) {
        return inventoryService.countShowSeats(showId);
    }

    @Override
    public List<ShowSeatSnapshotDTO> sell(long showId, String zone, int quantity, long orderNo) {
        return inventoryService.sellShowSeats(showId, zone, quantity, orderNo)
                .stream().map(this::toSnapshot).toList();
    }

    @Override
    public int rollbackSale(long orderNo) {
        return inventoryService.rollbackShowSale(orderNo);
    }

    private ShowSeatSnapshotDTO toSnapshot(ShowSeat seat) {
        ShowSeatSnapshotDTO dto = new ShowSeatSnapshotDTO();
        dto.setId(seat.getId());
        dto.setZone(seat.getZone());
        dto.setSeatNo(seat.getSeatNo());
        return dto;
    }
}
