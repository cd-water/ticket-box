package com.cdwater.ticketbox.payment.api;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.payment.api.dto.PaymentDTO;

import java.math.BigDecimal;

public interface PaymentApi {

    PaymentDTO create(long orderNo, BigDecimal amount, String channel) throws BizException;

    PaymentDTO queryByOrderNo(long orderNo) throws BizException;

    /** 幂等退款：已退款/未支付的支付单直接返回当前状态。 */
    PaymentDTO refundByOrderNo(long orderNo) throws BizException;
}
