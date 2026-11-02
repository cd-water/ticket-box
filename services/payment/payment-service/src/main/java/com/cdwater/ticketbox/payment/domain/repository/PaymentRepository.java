package com.cdwater.ticketbox.payment.domain.repository;

import com.cdwater.ticketbox.payment.domain.model.PaymentOrder;

import java.math.BigDecimal;
import java.util.Optional;

public interface PaymentRepository {

    Optional<PaymentOrder> findByOrderNo(Long orderNo);

    Optional<PaymentOrder> findByPaymentNo(String paymentNo);

    void save(PaymentOrder order);

    void saveTransaction(String paymentNo, String type, BigDecimal amount, String channelTxnNo);

    int markClosed(String paymentNo);
}
