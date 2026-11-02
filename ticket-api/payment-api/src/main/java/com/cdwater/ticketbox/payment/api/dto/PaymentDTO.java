package com.cdwater.ticketbox.payment.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String paymentNo;
    private Long orderNo;
    private BigDecimal amount;
    private String channel;
    private String status;
    private LocalDateTime payTime;
}
