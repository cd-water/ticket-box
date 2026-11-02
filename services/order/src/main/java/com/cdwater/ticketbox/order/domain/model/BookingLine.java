package com.cdwater.ticketbox.order.domain.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BookingLine {

    private Long orderNo;
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
