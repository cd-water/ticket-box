package com.cdwater.ticketbox.booking.application.port;

import java.util.Optional;

public interface MovieInfoPort {

    Optional<MovieInfo> findById(Long id);
}
