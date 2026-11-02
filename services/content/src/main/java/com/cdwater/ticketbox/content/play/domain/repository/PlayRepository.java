package com.cdwater.ticketbox.content.play.domain.repository;

import com.cdwater.ticketbox.content.play.domain.model.PlayItem;
import com.cdwater.ticketbox.common.result.PageResult;

import java.util.Optional;

public interface PlayRepository {

    PageResult<PlayItem> page(String keyword, long page, long size);

    Optional<PlayItem> findById(Long id);
}
