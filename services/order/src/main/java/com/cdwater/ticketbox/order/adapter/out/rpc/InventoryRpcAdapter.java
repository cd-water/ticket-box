package com.cdwater.ticketbox.order.adapter.out.rpc;

import com.cdwater.ticketbox.inventory.api.InventoryApi;
import com.cdwater.ticketbox.inventory.api.dto.HoldCommandDTO;
import com.cdwater.ticketbox.inventory.api.dto.HoldDTO;
import com.cdwater.ticketbox.order.application.port.InventoryPort;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryRpcAdapter implements InventoryPort {

    @DubboReference(check = false, timeout = 3000)
    private InventoryApi inventoryApi;

    @Override
    public HoldDTO hold(HoldCommandDTO command) {
        return inventoryApi.hold(command);
    }

    @Override
    public void confirm(long orderNo) {
        inventoryApi.confirm(orderNo);
    }

    @Override
    public void release(long orderNo) {
        inventoryApi.release(orderNo);
    }
}
