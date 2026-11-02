package com.cdwater.ticketbox.order.show.domain.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShowOrderSeat {

    private Long orderNo;
    private Long seatId;
    private String zone;
    private Integer seatNo;
    private BigDecimal price;
}
