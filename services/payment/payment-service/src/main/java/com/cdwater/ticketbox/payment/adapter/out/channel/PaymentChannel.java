package com.cdwater.ticketbox.payment.adapter.out.channel;

import java.math.BigDecimal;

public interface PaymentChannel {

    ChannelResult create(String paymentNo, BigDecimal amount);

    void close(String paymentNo);
}
