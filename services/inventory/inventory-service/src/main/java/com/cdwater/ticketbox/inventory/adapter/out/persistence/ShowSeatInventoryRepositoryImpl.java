package com.cdwater.ticketbox.inventory.adapter.out.persistence;

import com.cdwater.ticketbox.inventory.adapter.out.persistence.mapper.ShowSeatMapper;
import com.cdwater.ticketbox.inventory.adapter.out.persistence.projection.ZoneAvailabilityRow;
import com.cdwater.ticketbox.inventory.domain.model.ShowSeat;
import com.cdwater.ticketbox.inventory.domain.repository.ShowSeatInventoryRepository;
import com.cdwater.ticketbox.inventory.adapter.out.persistence.converter.ShowSeatConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ShowSeatInventoryRepositoryImpl implements ShowSeatInventoryRepository {

    private final ShowSeatMapper showSeatMapper;
    private final ShowSeatConverter showSeatConverter;

    @Override
    public Map<String, Long> countAvailableByZone(Long showId) {
        return showSeatMapper.countAvailableByZone(showId).stream()
                .collect(Collectors.toMap(ZoneAvailabilityRow::getZone,
                        ZoneAvailabilityRow::getAvailable));
    }

    @Override
    public List<ShowSeat> lockAvailableSeats(Long showId, String zone) {
        return showSeatConverter.toDomain(showSeatMapper.lockAvailableSeats(showId, zone));
    }

    @Override
    public List<ShowSeat> findSoldByOrder(Long orderNo) {
        return showSeatConverter.toDomain(showSeatMapper.selectSoldByOrder(orderNo));
    }

    @Override
    public int markSold(Collection<Long> seatIds, Long orderNo) {
        return showSeatMapper.markSold(seatIds, orderNo);
    }

    @Override
    public int rollbackSale(Long orderNo) {
        return showSeatMapper.rollbackSale(orderNo);
    }
}
