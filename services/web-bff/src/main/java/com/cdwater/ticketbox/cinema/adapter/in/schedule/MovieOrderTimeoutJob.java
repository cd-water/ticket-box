package com.cdwater.ticketbox.cinema.adapter.in.schedule;

import com.cdwater.ticketbox.cinema.application.command.MovieOrderCommandService;
import com.cdwater.ticketbox.cinema.domain.model.MovieOrder;
import com.cdwater.ticketbox.cinema.domain.repository.MovieOrderRepository;
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
public class MovieOrderTimeoutJob {

    private final MovieOrderRepository orderRepository;
    private final MovieOrderCommandService orderCommandService;

    @DubboReference(check = false, timeout = 3000)
    private PaymentApi paymentApi;

    @Scheduled(fixedDelay = 60_000)
    public void expireOrders() {
        List<MovieOrder> orders = orderRepository.findExpiredPending(LocalDateTime.now(), 100);
        for (MovieOrder order : orders) {
            try {
                PaymentDTO payment = paymentApi.query("P" + order.getOrderNo());
                if (payment != null && "SUCCESS".equals(payment.getStatus())) {
                    continue;
                }
                orderCommandService.expireOrder(order.getOrderNo());
            } catch (Exception e) {
                log.warn("expire order failed, orderNo={}", order.getOrderNo(), e);
            }
        }
    }
}
