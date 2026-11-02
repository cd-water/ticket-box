package com.cdwater.ticketbox.inventory.domain.repository;

import java.util.Collection;
import java.util.Map;

public interface PlayStockRepository {

    boolean deduct(Long itemId, int quantity);

    int restore(Long itemId, int quantity);

    Map<Long, Integer> getStocks(Collection<Long> itemIds);
}
