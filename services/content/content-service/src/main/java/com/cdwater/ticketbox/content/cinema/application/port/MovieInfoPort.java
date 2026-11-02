package com.cdwater.ticketbox.content.cinema.application.port;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface MovieInfoPort {

    Optional<MovieInfo> findById(Long id);

    Map<Long, MovieInfo> findByIds(Collection<Long> ids);
}
