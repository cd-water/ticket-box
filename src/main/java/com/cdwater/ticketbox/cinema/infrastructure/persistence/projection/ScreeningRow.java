package com.cdwater.ticketbox.cinema.infrastructure.persistence.projection;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ScreeningRow {

    private Long id;
    private Long hallId;
    private String hallName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal price;
}
