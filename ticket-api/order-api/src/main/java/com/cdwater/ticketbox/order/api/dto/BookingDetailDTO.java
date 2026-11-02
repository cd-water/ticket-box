package com.cdwater.ticketbox.order.api.dto;

import com.cdwater.ticketbox.common.ticket.ProductType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookingDetailDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String orderNo;
    private ProductType productType;
    /** 0-待支付 1-已支付 2-已取消（含超时） */
    private Integer status;
    private BigDecimal totalAmount;
    private LocalDateTime expireTime;
    private LocalDateTime payTime;
    private LocalDateTime createTime;
    private BookingLineDTO line;
    private List<BookingSeatDTO> seats;
}
