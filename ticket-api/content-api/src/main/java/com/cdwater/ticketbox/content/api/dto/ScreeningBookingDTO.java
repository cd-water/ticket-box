package com.cdwater.ticketbox.content.api.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScreeningBookingDTO  implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long screeningId;
    private Long cinemaId;
    private String cinemaName;
    private Long hallId;
    private String hallName;
    private Long movieId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal price;
    private Integer status;
}
