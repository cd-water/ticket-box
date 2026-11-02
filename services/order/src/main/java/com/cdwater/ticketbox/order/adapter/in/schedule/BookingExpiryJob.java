package com.cdwater.ticketbox.order.adapter.in.schedule;

import com.cdwater.ticketbox.order.application.command.BookingExpiryService;
import com.cdwater.ticketbox.order.domain.model.BookingOrder;
import com.cdwater.ticketbox.order.domain.repository.BookingOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingExpiryJob {

    private final BookingOrderRepository orderRepository;
    private final BookingExpiryService expiryService;

    @Scheduled(fixedDelay = 60_000)
    public void expireOrders() {
        List<BookingOrder> orders = orderRepository.findExpiredPending(LocalDateTime.now(), 100);
        for (BookingOrder order : orders) {
            try {
                expiryService.expireOrder(order.getOrderNo());
            } catch (RuntimeException e) {
                log.warn("expire booking order failed, orderNo={}", order.getOrderNo(), e);
            }
        }
    }
}
