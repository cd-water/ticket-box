package com.cdwater.ticketbox.order.show.application.command;

import com.cdwater.ticketbox.order.show.domain.repository.ShowOrderRepository;
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
public class ShowOrderExpiryService {

    private final ShowOrderRepository orderRepository;

    @DubboReference(check = false, timeout = 3000)
    private PaymentApi paymentApi;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void expire(Long orderNo) {
        PaymentDTO payment = paymentApi.queryByOrderNo(orderNo);
        if (payment != null && "SUCCESS".equals(payment.getStatus())) {
            log.warn("skip expiring paid show order {}", orderNo);
            return;
        }
        orderRepository.markCancelled(orderNo);
    }
}
