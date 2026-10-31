package com.cdwater.ticketbox.cinema.domain.repository;

import com.cdwater.ticketbox.cinema.domain.model.Cinema;
import com.cdwater.ticketbox.common.result.PageResult;

import java.util.Optional;

public interface CinemaRepository {

    PageResult<Cinema> page(String name, long page, long size);

    Optional<Cinema> findById(Long id);
}
