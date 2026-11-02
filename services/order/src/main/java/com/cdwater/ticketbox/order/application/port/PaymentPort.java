package com.cdwater.ticketbox.order.application.port;

import com.cdwater.ticketbox.payment.api.dto.PaymentDTO;

import java.math.BigDecimal;

public interface PaymentPort {

    PaymentDTO pay(long orderNo, BigDecimal amount);

    PaymentDTO find(long orderNo);

    PaymentDTO refund(long orderNo);
}
