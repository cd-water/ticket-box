package com.cdwater.ticketbox.show.application.query.view;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShowOrderSeatView {

    private Long seatId;
    private String zone;
    private Integer seatNo;
    private BigDecimal price;
}
