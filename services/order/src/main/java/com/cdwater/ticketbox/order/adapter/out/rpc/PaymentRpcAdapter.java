package com.cdwater.ticketbox.order.adapter.out.rpc;

import com.cdwater.ticketbox.order.application.port.PaymentPort;
import com.cdwater.ticketbox.payment.api.PaymentApi;
import com.cdwater.ticketbox.payment.api.dto.PaymentDTO;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class PaymentRpcAdapter implements PaymentPort {

    @DubboReference(check = false, timeout = 3000)
    private PaymentApi paymentApi;

    @Override
    public PaymentDTO pay(long orderNo, BigDecimal amount) {
        return paymentApi.create(orderNo, amount, "MOCK");
    }

    @Override
    public PaymentDTO find(long orderNo) {
        return paymentApi.queryByOrderNo(orderNo);
    }

    @Override
    public PaymentDTO refund(long orderNo) {
        return paymentApi.refundByOrderNo(orderNo);
    }
}
