package com.cdwater.ticketbox.inventory.application;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
class ShowSeatSellIdempotencyTest {

    private static final long ORDER_NO = 2900000000000000099L;

    @Autowired
    InventoryService inventoryService;

    @Autowired
    JdbcTemplate jdbc;

    @AfterEach
    void cleanUp() {
        jdbc.update("""
                UPDATE t_show_seat SET status = 0, lock_order_no = NULL, lock_expire_time = NULL
                WHERE lock_order_no = ?
                """, ORDER_NO);
    }

    @Test
    void retrying_sell_for_same_order_returns_same_seats() {
        var first = inventoryService.sellShowSeats(1L, "A区", 2, ORDER_NO);
        var second = inventoryService.sellShowSeats(1L, "A区", 2, ORDER_NO);

        assertEquals(first.stream().map(s -> s.getId()).toList(),
                second.stream().map(s -> s.getId()).toList());
        Integer sold = jdbc.queryForObject(
                "SELECT COUNT(*) FROM t_show_seat WHERE lock_order_no = ? AND status = 2",
                Integer.class, ORDER_NO);
        assertEquals(2, sold);
    }
}
