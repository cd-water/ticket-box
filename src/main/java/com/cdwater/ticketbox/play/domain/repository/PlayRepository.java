package com.cdwater.ticketbox.play.domain.repository;

import com.cdwater.ticketbox.play.domain.model.PlayItem;
import com.cdwater.ticketbox.shared.query.PageResult;

import java.util.Optional;

public interface PlayRepository {

    PageResult<PlayItem> page(String keyword, long page, long size);

    Optional<PlayItem> findById(Long id);

    boolean deductStock(Long itemId, int quantity);

    int restoreStock(Long itemId, int quantity);
}
