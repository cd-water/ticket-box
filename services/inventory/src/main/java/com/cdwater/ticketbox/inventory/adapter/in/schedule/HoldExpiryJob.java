package com.cdwater.ticketbox.inventory.adapter.in.schedule;

import com.cdwater.ticketbox.inventory.application.InventoryService;
import com.cdwater.ticketbox.inventory.domain.model.Hold;
import com.cdwater.ticketbox.inventory.domain.repository.HoldRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class HoldExpiryJob {

    private final HoldRepository holdRepository;
    private final InventoryService inventoryService;

    @Scheduled(fixedDelay = 60_000)
    public void releaseExpired() {
        List<Hold> holds = holdRepository.findExpiredHeld(LocalDateTime.now(), 100);
        for (Hold hold : holds) {
            try {
                inventoryService.release(hold.getOrderNo());
            } catch (RuntimeException e) {
                log.warn("release expired hold failed, orderNo={}", hold.getOrderNo(), e);
            }
        }
    }
}
