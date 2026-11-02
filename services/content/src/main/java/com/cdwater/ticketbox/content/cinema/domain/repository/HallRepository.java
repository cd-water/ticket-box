package com.cdwater.ticketbox.content.cinema.domain.repository;

import com.cdwater.ticketbox.content.cinema.domain.model.Hall;

import java.util.Optional;

public interface HallRepository {

    Optional<Hall> findById(Long id);
}
