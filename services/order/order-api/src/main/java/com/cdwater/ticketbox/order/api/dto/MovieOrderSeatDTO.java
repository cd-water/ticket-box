package com.cdwater.ticketbox.order.api.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class MovieOrderSeatDTO  implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long seatId;
    private Integer rowNum;
    private Integer colNum;
    private BigDecimal price;
}