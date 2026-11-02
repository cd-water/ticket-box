package com.cdwater.ticketbox.order.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BookingLineDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long productId;
    private Long ticketTypeId;
    private String title;
    private String venueName;
    private String venueAddress;
    private String hallName;
    private String ticketTypeName;
    private String zone;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal unitPrice;
    private Integer quantity;
}
