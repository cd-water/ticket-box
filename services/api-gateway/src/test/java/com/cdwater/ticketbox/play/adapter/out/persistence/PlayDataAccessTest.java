package com.cdwater.ticketbox.play.adapter.out.persistence;

import com.cdwater.ticketbox.play.domain.model.PlayItem;
import com.cdwater.ticketbox.play.domain.repository.PlayRepository;
import com.cdwater.ticketbox.common.result.PageResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlayDataAccessTest {

    @Autowired
    PlayRepository playRepository;

    @Test
    void list_and_filter() {
        PageResult<PlayItem> page = playRepository.page("", 1, 10);
        assertEquals(6, page.getTotal());
        assertEquals(1, playRepository.page("温泉", 1, 10).getTotal());
    }

    @Test
    void deduct_and_restore_stock() {
        PlayItem item = playRepository.findById(4L).orElseThrow();
        int before = item.getStock();
        assertTrue(playRepository.deductStock(4L, 2));
        assertEquals(before - 2, playRepository.findById(4L).orElseThrow().getStock());
        assertEquals(1, playRepository.restoreStock(4L, 2));
        assertEquals(before, playRepository.findById(4L).orElseThrow().getStock());
    }

    @Test
    void cannot_deduct_more_than_stock() {
        assertFalse(playRepository.deductStock(3L, 1000));
        assertTrue(playRepository.findById(3L).orElseThrow().getStock() >= 0);
    }
}
