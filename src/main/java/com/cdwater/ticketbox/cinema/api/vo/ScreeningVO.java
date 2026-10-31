package com.cdwater.ticketbox.cinema.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ScreeningVO {

    private Long id;
    private String hallName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal price;
    private long availableSeats;
}
