package com.cdwater.ticketbox.order;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ShowOrderView {

    private String orderNo;
    private Integer status;
    private BigDecimal totalAmount;
    private LocalDateTime expireTime;
    private LocalDateTime payTime;
    private LocalDateTime createTime;
    private String showTitle;
    private String venueName;
    private String venueAddress;
    private LocalDateTime startTime;
    private String ticketTypeName;
    private String zone;
    private BigDecimal unitPrice;
    private Integer quantity;
    private List<OrderSeatView> seats;
}
