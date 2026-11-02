package com.cdwater.ticketbox.home.application.query.view;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class HomeShowItem {

    private Long id;
    private String title;
    private String poster;
    private String venueName;
    private String venueAddress;
    private LocalDateTime startTime;
    private BigDecimal minPrice;
}
