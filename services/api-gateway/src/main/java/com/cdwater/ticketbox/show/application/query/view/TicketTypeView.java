package com.cdwater.ticketbox.show.application.query.view;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TicketTypeView {

    private Long id;
    private String name;
    private String zone;
    private BigDecimal price;
    private long availableSeats;
}
