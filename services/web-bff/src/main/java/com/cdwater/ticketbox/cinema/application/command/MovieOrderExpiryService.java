package com.cdwater.ticketbox.cinema.application.command;

import com.cdwater.ticketbox.cinema.domain.repository.MovieOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MovieOrderExpiryService {

    private final MovieOrderRepository orderRepository;
    private final SeatInventoryService seatInventoryService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void expireOrder(Long orderNo) {
        if (orderRepository.markCancelled(orderNo)) {
            seatInventoryService.releaseByOrder(orderNo);
        }
    }
}
