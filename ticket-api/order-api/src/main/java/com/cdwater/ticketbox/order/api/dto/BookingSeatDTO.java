package com.cdwater.ticketbox.order.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class BookingSeatDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long seatId;
    private String zone;
    private Integer seatNo;
    private Integer rowNum;
    private Integer colNum;
    private BigDecimal price;
}
