package com.cdwater.ticketbox.order;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderSeatView {

    private Long seatId;
    private String zone;
    private Integer seatNo;
    private Integer rowNum;
    private Integer colNum;
    private BigDecimal price;
}
