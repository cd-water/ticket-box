package com.cdwater.ticketbox.play.application.command;

import com.cdwater.ticketbox.play.domain.model.PlayOrder;
import com.cdwater.ticketbox.play.domain.model.PlayOrderStatus;
import com.cdwater.ticketbox.play.domain.repository.PlayOrderRepository;
import com.cdwater.ticketbox.inventory.api.PlayStockApi;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlayOrderExpiryService {

    private final PlayOrderRepository orderRepository;

    @DubboReference(check = false, timeout = 3000)
    private PlayStockApi playStockApi;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void expire(Long orderNo) {
        PlayOrder order = orderRepository.findByOrderNo(orderNo).orElse(null);
        if (order == null || order.getStatus() != PlayOrderStatus.PENDING.getCode()) {
            return;
        }
        if (orderRepository.markCancelled(orderNo)) {
            playStockApi.restore(order.getItemId(), order.getQuantity());
        }
    }
}
