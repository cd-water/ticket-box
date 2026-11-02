package com.cdwater.ticketbox.order.api.dto;

import java.io.Serializable;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MovieOrderListItemDTO  implements Serializable {

    private static final long serialVersionUID = 1L;

    private String orderNo;
    private Integer status;
    private BigDecimal totalAmount;
    private LocalDateTime expireTime;
    private LocalDateTime payTime;
    private LocalDateTime createTime;
    private String movieTitle;
    private String cinemaName;
    private String hallName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}