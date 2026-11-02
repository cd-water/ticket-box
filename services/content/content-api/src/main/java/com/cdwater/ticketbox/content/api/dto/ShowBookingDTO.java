package com.cdwater.ticketbox.content.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowBookingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long showId;
    private String title;
    private String venueName;
    private String venueAddress;
    private LocalDateTime startTime;
    private Long ticketTypeId;
    private String ticketTypeName;
    private String zone;
    private BigDecimal price;
    private boolean upcoming;
}
