package com.cdwater.ticketbox.order.api.dto;

import com.cdwater.ticketbox.common.ticket.ProductType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BookingListItemDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String orderNo;
    private ProductType productType;
    private Integer status;
    private BigDecimal totalAmount;
    private LocalDateTime expireTime;
    private LocalDateTime payTime;
    private LocalDateTime createTime;
    private BookingLineDTO line;
}
