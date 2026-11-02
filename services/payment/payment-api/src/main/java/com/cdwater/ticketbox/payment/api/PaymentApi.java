package com.cdwater.ticketbox.payment.api;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.payment.api.dto.PaymentDTO;

import java.math.BigDecimal;

public interface PaymentApi {

    PaymentDTO create(long orderNo, BigDecimal amount, String channel) throws BizException;

    PaymentDTO query(String paymentNo) throws BizException;

    void close(String paymentNo) throws BizException;
}
