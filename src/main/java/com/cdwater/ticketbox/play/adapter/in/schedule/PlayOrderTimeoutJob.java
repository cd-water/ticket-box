package com.cdwater.ticketbox.play.adapter.in.schedule;

import com.cdwater.ticketbox.play.application.command.PlayOrderExpiryService;
import com.cdwater.ticketbox.play.domain.model.PlayOrder;
import com.cdwater.ticketbox.play.domain.repository.PlayOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlayOrderTimeoutJob {

    private final PlayOrderRepository orderRepository;
    private final PlayOrderExpiryService expiryService;

    @Scheduled(fixedDelay = 60_000)
    public void expireOrders() {
        List<PlayOrder> orders = orderRepository.findExpiredPending(LocalDateTime.now(), 100);
        for (PlayOrder order : orders) {
            try {
                expiryService.expire(order.getOrderNo());
            } catch (Exception e) {
                log.warn("expire play order failed, orderNo={}", order.getOrderNo(), e);
            }
        }
    }
}
