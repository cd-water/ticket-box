package com.cdwater.ticketbox.cinema.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ScreeningBookingInfo(
        Long screeningId,
        Long cinemaId,
        String cinemaName,
        Long hallId,
        String hallName,
        Long movieId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        BigDecimal price,
        Integer status) {
}
