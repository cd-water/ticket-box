package com.cdwater.ticketbox.content.api.dto;

import java.io.Serializable;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SeatMapDTO  implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long screeningId;
    private String cinemaName;
    private String movieTitle;
    private String hallName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal price;
    private int rowCount;
    private int colCount;
    private List<SeatDTO> seats;
}
