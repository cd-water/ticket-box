package com.cdwater.ticketbox.order.show.adapter.in.schedule;

import com.cdwater.ticketbox.order.show.application.command.ShowOrderCommandService;
import com.cdwater.ticketbox.order.show.domain.model.ShowOrder;
import com.cdwater.ticketbox.order.show.domain.repository.ShowOrderRepository;
import com.cdwater.ticketbox.payment.api.PaymentApi;
import com.cdwater.ticketbox.payment.api.dto.PaymentDTO;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShowOrderTimeoutJob {

    private final ShowOrderRepository orderRepository;
    private final ShowOrderCommandService orderCommandService;

    @DubboReference(check = false, timeout = 3000)
    private PaymentApi paymentApi;

    @Scheduled(fixedDelay = 60_000)
    public void expireOrders() {
        List<ShowOrder> orders = orderRepository.findExpiredPending(LocalDateTime.now(), 100);
        for (ShowOrder order : orders) {
            try {
                PaymentDTO payment = paymentApi.queryByOrderNo(order.getOrderNo());
                if (payment != null && "SUCCESS".equals(payment.getStatus())) {
                    continue;
                }
                orderCommandService.expire(order.getOrderNo());
            } catch (Exception e) {
                log.warn("expire show order failed, orderNo={}", order.getOrderNo(), e);
            }
        }
    }
}
