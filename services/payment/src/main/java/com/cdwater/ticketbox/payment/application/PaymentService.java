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
    public static final String REFUNDED = "REFUNDED";

    private final PaymentRepository repository;
    private final PaymentChannel channel;

    @Transactional
    public PaymentOrder create(Long orderNo, BigDecimal amount, String channelName) {
        Optional<PaymentOrder> existing = repository.findByOrderNo(orderNo);
        if (existing.isPresent()) {
            return existing.get();
        }

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
        repository.saveTransaction(paymentNo, "PAY", amount, result.channelTransactionNo());
        return order;
    }

    public Optional<PaymentOrder> findByOrderNo(Long orderNo) {
        return repository.findByOrderNo(orderNo);
    }

    /** 幂等退款：仅 SUCCESS 的支付单会触发渠道退款与状态迁移。 */
    @Transactional
    public Optional<PaymentOrder> refundByOrderNo(Long orderNo) {
        Optional<PaymentOrder> existing = repository.findByOrderNo(orderNo);
        if (existing.isEmpty()) {
            return Optional.empty();
        }
        PaymentOrder order = existing.get();
        if (!SUCCESS.equals(order.getStatus())) {
            return existing;
        }
        channel.refund(order.getPaymentNo(), order.getAmount());
        repository.markRefunded(order.getPaymentNo());
        repository.saveTransaction(order.getPaymentNo(), "REFUND",
                order.getAmount(), "REFUND-" + order.getPaymentNo());
        order.setStatus(REFUNDED);
        return Optional.of(order);
    }

}
