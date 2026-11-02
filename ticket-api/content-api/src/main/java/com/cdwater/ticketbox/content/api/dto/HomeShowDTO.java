package com.cdwater.ticketbox.content.api.dto;

import java.io.Serializable;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class HomeShowDTO  implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String title;
    private String poster;
    private String venueName;
    private String venueAddress;
    private LocalDateTime startTime;
    private BigDecimal minPrice;
}
