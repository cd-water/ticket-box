package com.cdwater.ticketbox.order.play.application.command;

import com.cdwater.ticketbox.order.play.domain.model.PlayOrder;
import com.cdwater.ticketbox.order.play.domain.model.PlayOrderStatus;
import com.cdwater.ticketbox.order.play.domain.repository.PlayOrderRepository;
import com.cdwater.ticketbox.inventory.api.PlayStockApi;
import com.cdwater.ticketbox.payment.api.PaymentApi;
import com.cdwater.ticketbox.payment.api.dto.PaymentDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayOrderExpiryService {

    private final PlayOrderRepository orderRepository;

    @DubboReference(check = false, timeout = 3000)
    private PlayStockApi playStockApi;

    @DubboReference(check = false, timeout = 3000)
    private PaymentApi paymentApi;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void expire(Long orderNo) {
        PaymentDTO payment = paymentApi.queryByOrderNo(orderNo);
        if (payment != null && "SUCCESS".equals(payment.getStatus())) {
            log.warn("skip expiring paid play order {}", orderNo);
            return;
        }
        PlayOrder order = orderRepository.findByOrderNo(orderNo).orElse(null);
        if (order == null || order.getStatus() != PlayOrderStatus.PENDING.getCode()) {
            return;
        }
        if (orderRepository.markCancelled(orderNo)) {
            try {
                playStockApi.restore(order.getItemId(), order.getQuantity());
            } catch (RuntimeException ex) {
                log.warn("restore play stock failed, orderNo={}", orderNo, ex);
            }
        }
    }
}
