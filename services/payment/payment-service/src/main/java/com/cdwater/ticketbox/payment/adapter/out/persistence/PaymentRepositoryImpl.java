package com.cdwater.ticketbox.payment.adapter.out.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cdwater.ticketbox.payment.adapter.out.persistence.mapper.PaymentOrderMapper;
import com.cdwater.ticketbox.payment.adapter.out.persistence.mapper.PaymentTransactionMapper;
import com.cdwater.ticketbox.payment.adapter.out.persistence.po.PaymentOrderPO;
import com.cdwater.ticketbox.payment.adapter.out.persistence.po.PaymentTransactionPO;
import com.cdwater.ticketbox.payment.domain.model.PaymentOrder;
import com.cdwater.ticketbox.payment.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentOrderMapper orderMapper;
    private final PaymentTransactionMapper transactionMapper;

    @Override
    public Optional<PaymentOrder> findByOrderNo(Long orderNo) {
        return Optional.ofNullable(orderMapper.selectOne(
                new LambdaQueryWrapper<PaymentOrderPO>().eq(PaymentOrderPO::getOrderNo, orderNo)))
                .map(this::toDomain);
    }

    @Override
    public Optional<PaymentOrder> findByPaymentNo(String paymentNo) {
        return Optional.ofNullable(orderMapper.selectOne(
                new LambdaQueryWrapper<PaymentOrderPO>().eq(PaymentOrderPO::getPaymentNo, paymentNo)))
                .map(this::toDomain);
    }

    @Override
    public void save(PaymentOrder order) {
        PaymentOrderPO po = new PaymentOrderPO();
        po.setPaymentNo(order.getPaymentNo());
        po.setOrderNo(order.getOrderNo());
        po.setAmount(order.getAmount());
        po.setChannel(order.getChannel());
        po.setStatus(order.getStatus());
        po.setIdempotencyKey(order.getIdempotencyKey());
        po.setCreateTime(order.getCreateTime());
        po.setPayTime(order.getPayTime());
        orderMapper.insert(po);
        order.setId(po.getId());
    }

    @Override
    public void saveTransaction(String paymentNo, String type, BigDecimal amount, String channelTxnNo) {
        PaymentTransactionPO po = new PaymentTransactionPO();
        po.setPaymentNo(paymentNo);
        po.setType(type);
        po.setAmount(amount);
        po.setChannelTxnNo(channelTxnNo);
        po.setCreateTime(LocalDateTime.now());
        transactionMapper.insert(po);
    }

    @Override
    public int markClosed(String paymentNo) {
        return orderMapper.markClosed(paymentNo);
    }

    @Override
    public int reopen(String paymentNo) {
        return orderMapper.reopen(paymentNo, LocalDateTime.now());
    }

    private PaymentOrder toDomain(PaymentOrderPO po) {
        PaymentOrder order = new PaymentOrder();
        order.setId(po.getId());
        order.setPaymentNo(po.getPaymentNo());
        order.setOrderNo(po.getOrderNo());
        order.setAmount(po.getAmount());
        order.setChannel(po.getChannel());
        order.setStatus(po.getStatus());
        order.setIdempotencyKey(po.getIdempotencyKey());
        order.setCreateTime(po.getCreateTime());
        order.setPayTime(po.getPayTime());
        return order;
    }
}
