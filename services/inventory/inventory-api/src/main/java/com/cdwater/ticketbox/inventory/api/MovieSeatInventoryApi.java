package com.cdwater.ticketbox.inventory.api;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.inventory.api.dto.MovieSeatDTO;
import com.cdwater.ticketbox.inventory.api.dto.MovieSeatSnapshotDTO;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface MovieSeatInventoryApi {

    List<MovieSeatDTO> seatMap(long screeningId);

    Map<Long, Long> countAvailable(Collection<Long> screeningIds);

    List<MovieSeatSnapshotDTO> reserve(long screeningId, List<Long> seatIds,
                                       long orderNo, LocalDateTime expireAt) throws BizException;

    int confirm(long orderNo);

    int rollbackSale(long orderNo);

    void release(long orderNo);

    void releaseExpired(long screeningId);
}
