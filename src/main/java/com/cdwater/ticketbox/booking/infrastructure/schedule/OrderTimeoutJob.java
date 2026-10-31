package com.cdwater.ticketbox.booking.infrastructure.schedule;

import com.cdwater.ticketbox.booking.application.service.OrderApplicationService;
import com.cdwater.ticketbox.booking.domain.model.Order;
import com.cdwater.ticketbox.booking.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderTimeoutJob {

    private final OrderRepository orderRepository;
    private final OrderApplicationService orderApplicationService;

    @Scheduled(fixedDelay = 60_000)
    public void expireOrders() {
        List<Order> orders = orderRepository.findExpiredPending(LocalDateTime.now(), 100);
        for (Order order : orders) {
            try {
                orderApplicationService.expireOrder(order.getId());
            } catch (Exception e) {
                log.warn("expire order failed, orderId={}", order.getId(), e);
            }
        }
    }
}
