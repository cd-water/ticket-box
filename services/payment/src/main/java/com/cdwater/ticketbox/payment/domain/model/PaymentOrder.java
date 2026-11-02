package com.cdwater.ticketbox.payment.domain.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentOrder {

    private Long id;
    private String paymentNo;
    private Long orderNo;
    private BigDecimal amount;
    private String channel;
    private String status;
    private String idempotencyKey;
    private LocalDateTime createTime;
    private LocalDateTime payTime;
}
