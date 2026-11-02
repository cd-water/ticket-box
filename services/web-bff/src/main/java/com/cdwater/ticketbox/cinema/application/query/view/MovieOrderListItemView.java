package com.cdwater.ticketbox.cinema.application.query.view;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MovieOrderListItemView {

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