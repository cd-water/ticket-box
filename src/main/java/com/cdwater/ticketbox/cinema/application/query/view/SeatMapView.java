package com.cdwater.ticketbox.cinema.application.query.view;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SeatMapView {

    private Long screeningId;
    private String cinemaName;
    private String movieTitle;
    private String hallName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal price;
    private int rowCount;
    private int colCount;
    private List<SeatView> seats;
}
