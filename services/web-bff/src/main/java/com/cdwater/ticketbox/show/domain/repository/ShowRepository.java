package com.cdwater.ticketbox.show.domain.repository;

import com.cdwater.ticketbox.show.domain.model.Show;
import com.cdwater.ticketbox.show.domain.model.ShowTicketType;
import com.cdwater.ticketbox.common.result.PageResult;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ShowRepository {

    PageResult<Show> page(String keyword, long page, long size);

    Optional<Show> findById(Long id);

    List<ShowTicketType> findTicketTypes(Long showId);

    Optional<ShowTicketType> findTicketType(Long showId, Long ticketTypeId);

    Map<Long, BigDecimal> findMinPrices(Collection<Long> showIds);
}
