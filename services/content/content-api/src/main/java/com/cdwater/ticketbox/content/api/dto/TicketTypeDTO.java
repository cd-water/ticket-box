package com.cdwater.ticketbox.content.api.dto;

import java.io.Serializable;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TicketTypeDTO  implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String zone;
    private BigDecimal price;
    private long availableSeats;
}
