package com.cdwater.ticketbox.inventory.api;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.inventory.api.dto.ShowSeatSnapshotDTO;

import java.util.List;
import java.util.Map;

public interface ShowSeatInventoryApi {

    Map<String, Long> countAvailable(long showId);

    List<ShowSeatSnapshotDTO> sell(long showId, String zone, int quantity, long orderNo)
            throws BizException;

    int rollbackSale(long orderNo);
}
