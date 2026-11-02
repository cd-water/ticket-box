package com.cdwater.ticketbox.inventory.adapter.in.rpc;

import com.cdwater.ticketbox.inventory.api.PlayStockApi;
import com.cdwater.ticketbox.inventory.application.InventoryService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.Collection;
import java.util.Map;

@DubboService
@RequiredArgsConstructor
public class PlayStockApiImpl implements PlayStockApi {

    private final InventoryService inventoryService;

    @Override
    public boolean deduct(long itemId, int quantity) {
        return inventoryService.deductPlayStock(itemId, quantity);
    }

    @Override
    public int restore(long itemId, int quantity) {
        return inventoryService.restorePlayStock(itemId, quantity);
    }

    @Override
    public Map<Long, Integer> getStocks(Collection<Long> itemIds) {
        return inventoryService.playStocks(itemIds);
    }
}
