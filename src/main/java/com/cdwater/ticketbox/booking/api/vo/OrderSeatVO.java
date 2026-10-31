package com.cdwater.ticketbox.booking.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class OrderSeatVO {

    private Long seatId;
    private Integer rowNum;
    private Integer colNum;
    private BigDecimal price;
}
