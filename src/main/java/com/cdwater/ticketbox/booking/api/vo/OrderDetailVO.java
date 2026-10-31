package com.cdwater.ticketbox.booking.api.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDetailVO {

    private Long id;
    private String orderNo;
    private Integer status;
    private BigDecimal totalAmount;
    private LocalDateTime expireTime;
    private LocalDateTime createTime;
    private String cinemaName;
    private String movieTitle;
    private String hallName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<OrderSeatVO> seats;
}
