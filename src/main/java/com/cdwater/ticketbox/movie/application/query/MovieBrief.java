package com.cdwater.ticketbox.movie.application.query;

import java.time.LocalDateTime;

public record MovieBrief(
        Long id,
        String title,
        String poster,
        LocalDateTime releaseDate,
        Integer duration,
        Integer status) {
}
