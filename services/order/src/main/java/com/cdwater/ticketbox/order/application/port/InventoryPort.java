package com.cdwater.ticketbox.order.application.port;

import com.cdwater.ticketbox.inventory.api.dto.HoldCommandDTO;
import com.cdwater.ticketbox.inventory.api.dto.HoldDTO;

public interface InventoryPort {

    HoldDTO hold(HoldCommandDTO command);

    void confirm(long orderNo);

    void release(long orderNo);
}
