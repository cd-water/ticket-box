package com.cdwater.ticketbox.payment.domain.repository;

import com.cdwater.ticketbox.payment.domain.model.PaymentOrder;

import java.math.BigDecimal;
import java.util.Optional;

public interface PaymentRepository {

    Optional<PaymentOrder> findByOrderNo(Long orderNo);

    void save(PaymentOrder order);

    void saveTransaction(String paymentNo, String transactionType, BigDecimal amount, String channelTransactionNo);

    int markRefunded(String paymentNo);
}
