package com.cdwater.ticketbox.payment.application;

import com.cdwater.ticketbox.payment.adapter.out.channel.ChannelResult;
import com.cdwater.ticketbox.payment.adapter.out.channel.PaymentChannel;
import com.cdwater.ticketbox.payment.domain.model.PaymentOrder;
import com.cdwater.ticketbox.payment.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    public static final String SUCCESS = "SUCCESS";
    public static final String CLOSED = "CLOSED";

    private final PaymentRepository repository;
    private final PaymentChannel channel;

    @Transactional
    public PaymentOrder create(Long orderNo, BigDecimal amount, String channelName) {
        return repository.findByOrderNo(orderNo).orElseGet(() -> {
            String paymentNo = "P" + orderNo;
            LocalDateTime now = LocalDateTime.now();
            PaymentOrder order = new PaymentOrder();
            order.setPaymentNo(paymentNo);
            order.setOrderNo(orderNo);
            order.setAmount(amount);
            order.setChannel(channelName);
            order.setStatus(SUCCESS);
            order.setIdempotencyKey("ORDER-" + orderNo);
            order.setCreateTime(now);
            order.setPayTime(now);
            repository.save(order);
            ChannelResult result = channel.create(paymentNo, amount);
            repository.saveTransaction(paymentNo, "PAY", amount, result.channelTxnNo());
            return order;
        });
    }

    public Optional<PaymentOrder> find(String paymentNo) {
        return repository.findByPaymentNo(paymentNo);
    }

    @Transactional
    public void close(String paymentNo) {
        repository.markClosed(paymentNo);
    }
}
