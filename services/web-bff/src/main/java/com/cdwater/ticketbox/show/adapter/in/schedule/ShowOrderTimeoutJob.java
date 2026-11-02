package com.cdwater.ticketbox.show.adapter.in.schedule;

import com.cdwater.ticketbox.show.application.command.ShowOrderCommandService;
import com.cdwater.ticketbox.show.domain.model.ShowOrder;
import com.cdwater.ticketbox.show.domain.repository.ShowOrderRepository;
import lombok.RequiredArgsConstructor;
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

    @Scheduled(fixedDelay = 60_000)
    public void expireOrders() {
        List<ShowOrder> orders = orderRepository.findExpiredPending(LocalDateTime.now(), 100);
        for (ShowOrder order : orders) {
            try {
                orderCommandService.expire(order.getOrderNo());
            } catch (Exception e) {
                log.warn("expire show order failed, orderNo={}", order.getOrderNo(), e);
            }
        }
    }
}
