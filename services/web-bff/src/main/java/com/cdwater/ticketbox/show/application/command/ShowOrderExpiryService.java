package com.cdwater.ticketbox.show.application.command;

import com.cdwater.ticketbox.show.domain.repository.ShowOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShowOrderExpiryService {

    private final ShowOrderRepository orderRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void expire(Long orderNo) {
        orderRepository.markCancelled(orderNo);
    }
}
