package com.cdwater.ticketbox.inventory.adapter.out.persistence;

import com.cdwater.ticketbox.inventory.adapter.out.persistence.mapper.PlayStockMapper;
import com.cdwater.ticketbox.inventory.adapter.out.persistence.projection.PlayStockRow;
import com.cdwater.ticketbox.inventory.domain.repository.PlayStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PlayStockRepositoryImpl implements PlayStockRepository {

    private final PlayStockMapper playStockMapper;

    @Override
    public boolean deduct(Long itemId, int quantity) {
        return playStockMapper.deduct(itemId, quantity) == 1;
    }

    @Override
    public int restore(Long itemId, int quantity) {
        return playStockMapper.restore(itemId, quantity);
    }

    @Override
    public Map<Long, Integer> getStocks(Collection<Long> itemIds) {
        if (itemIds == null || itemIds.isEmpty()) {
            return Map.of();
        }
        return playStockMapper.selectStocks(itemIds).stream()
                .collect(Collectors.toMap(PlayStockRow::getItemId, PlayStockRow::getStock));
    }
}
