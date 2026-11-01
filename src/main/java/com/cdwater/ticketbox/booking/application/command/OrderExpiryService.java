package com.cdwater.ticketbox.booking.application.command;

import com.cdwater.ticketbox.booking.application.port.SeatInventoryPort;
import com.cdwater.ticketbox.booking.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderExpiryService {

    private final OrderRepository orderRepository;
    private final SeatInventoryPort seatInventoryPort;

    @Transactional
    public void expireOrder(Long orderNo) {
        if (orderRepository.markTimeout(orderNo)) {
            seatInventoryPort.releaseByOrder(orderNo);
        }
    }
}
