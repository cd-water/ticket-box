package com.cdwater.ticketbox.payment.adapter.out.channel;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MockPaymentChannel implements PaymentChannel {

    @Override
    public ChannelResult create(String paymentNo, BigDecimal amount) {
        return new ChannelResult(true, "MOCK-" + paymentNo);
    }

    @Override
    public void close(String paymentNo) {
        // Mock 渠道没有外部动作
    }
}
