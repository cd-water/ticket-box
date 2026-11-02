package com.cdwater.ticketbox.content.cinema.domain.repository;

import com.cdwater.ticketbox.content.cinema.domain.model.Cinema;
import com.cdwater.ticketbox.common.result.PageResult;

import java.util.Optional;

public interface CinemaRepository {

    PageResult<Cinema> page(String name, Long movieId, long page, long size);

    Optional<Cinema> findById(Long id);
}
