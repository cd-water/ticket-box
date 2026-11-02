package com.cdwater.ticketbox.inventory.application;

import com.cdwater.ticketbox.common.ticket.ProductType;
import com.cdwater.ticketbox.inventory.api.dto.HoldCommandDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;

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
        jdbc.update("DELETE FROM t_hold WHERE order_no = ?", ORDER_NO);
    }

    @Test
    void retrying_hold_for_same_order_returns_same_seats() {
        HoldCommandDTO command = new HoldCommandDTO();
        command.setProductType(ProductType.SHOW);
        command.setProductId(1L);
        command.setZone("A区");
        command.setQuantity(2);
        command.setOrderNo(ORDER_NO);
        command.setExpireTime(LocalDateTime.now().plusMinutes(5));

        var first = inventoryService.hold(command);
        var second = inventoryService.hold(command);

        assertEquals(first.getUnits().stream().map(u -> u.getSeatId()).toList(),
                second.getUnits().stream().map(u -> u.getSeatId()).toList());

        inventoryService.confirm(ORDER_NO);
        Integer sold = jdbc.queryForObject(
                "SELECT COUNT(*) FROM t_show_seat WHERE lock_order_no = ? AND status = 2",
                Integer.class, ORDER_NO);
        assertEquals(2, sold);
    }
}
