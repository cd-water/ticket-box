package com.cdwater.ticketbox.inventory.api;

import java.util.Collection;
import java.util.Map;

public interface PlayStockApi {

    boolean deduct(long itemId, int quantity);

    int restore(long itemId, int quantity);

    Map<Long, Integer> getStocks(Collection<Long> itemIds);
}
