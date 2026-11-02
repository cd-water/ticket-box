package com.cdwater.ticketbox.order.domain.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookingSeat {

    private Long orderNo;
    private Long seatId;
    private String zone;
    private Integer seatNo;
    private Integer rowNum;
    private Integer colNum;
    private BigDecimal price;
}
