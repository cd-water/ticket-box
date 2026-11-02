package com.cdwater.ticketbox.order.movie.application.command;

import com.cdwater.ticketbox.order.movie.domain.repository.MovieOrderRepository;
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
public class MovieOrderExpiryService {

    private final MovieOrderRepository orderRepository;
    private final SeatInventoryService seatInventoryService;

    @DubboReference(check = false, timeout = 3000)
    private PaymentApi paymentApi;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void expireOrder(Long orderNo) {
        PaymentDTO payment = paymentApi.queryByOrderNo(orderNo);
        if (payment != null && "SUCCESS".equals(payment.getStatus())) {
            log.warn("skip expiring paid movie order {}", orderNo);
            return;
        }
        if (orderRepository.markCancelled(orderNo)) {
            seatInventoryService.releaseByOrder(orderNo);
        }
    }
}
