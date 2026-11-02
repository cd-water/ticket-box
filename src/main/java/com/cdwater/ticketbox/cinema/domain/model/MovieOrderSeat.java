package com.cdwater.ticketbox.cinema.domain.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MovieOrderSeat {

    private Long orderNo;
    private Long seatId;
    private Integer rowNum;
    private Integer colNum;
    private BigDecimal price;
}
