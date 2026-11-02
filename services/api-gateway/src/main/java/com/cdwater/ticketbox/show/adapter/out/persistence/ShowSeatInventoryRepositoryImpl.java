package com.cdwater.ticketbox.show.adapter.out.persistence;

import com.cdwater.ticketbox.show.adapter.out.persistence.mapper.ShowSeatMapper;
import com.cdwater.ticketbox.show.domain.model.ShowSeat;
import com.cdwater.ticketbox.show.domain.repository.ShowSeatInventoryRepository;
import com.cdwater.ticketbox.show.adapter.out.persistence.converter.ShowSeatConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ShowSeatInventoryRepositoryImpl implements ShowSeatInventoryRepository {

    private final ShowSeatMapper showSeatMapper;
    private final ShowSeatConverter showSeatConverter;

    @Override
    public List<ShowSeat> lockAvailableSeats(Long showId, String zone) {
        return showSeatConverter.toDomain(showSeatMapper.lockAvailableSeats(showId, zone));
    }

    @Override
    public int markSold(Collection<Long> seatIds, Long orderNo) {
        return showSeatMapper.markSold(seatIds, orderNo);
    }
}
