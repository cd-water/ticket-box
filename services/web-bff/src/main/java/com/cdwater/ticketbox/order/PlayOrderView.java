package com.cdwater.ticketbox.order;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PlayOrderView {

    private String orderNo;
    private Integer status;
    private BigDecimal totalAmount;
    private LocalDateTime expireTime;
    private LocalDateTime payTime;
    private LocalDateTime createTime;
    private String itemTitle;
    private String itemAddress;
    private BigDecimal unitPrice;
    private Integer quantity;
}
