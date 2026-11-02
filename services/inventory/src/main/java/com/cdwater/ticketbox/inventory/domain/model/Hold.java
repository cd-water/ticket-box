package com.cdwater.ticketbox.inventory.domain.model;

import com.cdwater.ticketbox.common.ticket.ProductType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Hold {

    private Long orderNo;
    private ProductType productType;
    private Long productId;
    private String zone;
    private Integer quantity;
    private Integer status;
    private LocalDateTime expireTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
