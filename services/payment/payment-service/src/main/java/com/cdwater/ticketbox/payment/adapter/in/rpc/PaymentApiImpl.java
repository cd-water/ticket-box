package com.cdwater.ticketbox.payment.adapter.in.rpc;

import com.cdwater.ticketbox.payment.api.PaymentApi;
import com.cdwater.ticketbox.payment.api.dto.PaymentDTO;
import com.cdwater.ticketbox.payment.application.PaymentService;
import com.cdwater.ticketbox.payment.domain.model.PaymentOrder;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

import java.math.BigDecimal;

@DubboService
@RequiredArgsConstructor
public class PaymentApiImpl implements PaymentApi {

    private final PaymentService paymentService;

    @Override
    public PaymentDTO create(long orderNo, BigDecimal amount, String channel) {
        return toDTO(paymentService.create(orderNo, amount, channel));
    }

    @Override
    public PaymentDTO query(String paymentNo) {
        return paymentService.find(paymentNo).map(this::toDTO).orElse(null);
    }

    @Override
    public void close(String paymentNo) {
        paymentService.close(paymentNo);
    }

    private PaymentDTO toDTO(PaymentOrder order) {
        return new PaymentDTO(order.getPaymentNo(), order.getOrderNo(), order.getAmount(),
                order.getChannel(), order.getStatus(), order.getPayTime());
    }
}
