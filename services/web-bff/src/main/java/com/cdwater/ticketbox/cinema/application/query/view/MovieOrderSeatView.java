package com.cdwater.ticketbox.cinema.application.query.view;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class MovieOrderSeatView {

    private Long seatId;
    private Integer rowNum;
    private Integer colNum;
    private BigDecimal price;
}