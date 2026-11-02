package com.cdwater.ticketbox.cinema.domain.repository;

import com.cdwater.ticketbox.cinema.domain.model.Hall;

import java.util.Optional;

public interface HallRepository {

    Optional<Hall> findById(Long id);
}
