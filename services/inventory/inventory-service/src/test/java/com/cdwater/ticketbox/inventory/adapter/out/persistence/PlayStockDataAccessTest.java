package com.cdwater.ticketbox.inventory.adapter.out.persistence;

import com.cdwater.ticketbox.inventory.domain.repository.PlayStockRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
class PlayStockDataAccessTest {

    @Autowired
    PlayStockRepository repository;

    @Autowired
    JdbcTemplate jdbc;

    private int stockOf(long itemId) {
        return jdbc.queryForObject(
                "SELECT stock FROM t_play_stock WHERE item_id = ?", Integer.class, itemId);
    }

    @Test
    void deduct_and_restore_stock() {
        int before = stockOf(4L);
        assertTrue(repository.deduct(4L, 2));
        assertEquals(before - 2, stockOf(4L));
        assertEquals(1, repository.restore(4L, 2));
        assertEquals(before, stockOf(4L));
    }

    @Test
    void cannot_deduct_more_than_stock() {
        int before = stockOf(3L);
        assertFalse(repository.deduct(3L, before + 1));
        assertEquals(before, stockOf(3L));
    }
}
