package com.cdwater.ticketbox.order.api.dto;

import java.io.Serializable;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShowOrderSeatDTO  implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long seatId;
    private String zone;
    private Integer seatNo;
    private BigDecimal price;
}