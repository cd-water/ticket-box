package com.cdwater.ticketbox.booking.adapter.in.schedule;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderTimeoutJobTest {

    @Autowired
    JdbcTemplate jdbc;

    @Autowired
    OrderTimeoutJob timeoutJob;

    Long orderId;

    @AfterEach
    void cleanUp() {
        if (orderId != null) {
            jdbc.update("""
                    UPDATE t_screening_seat
                    SET status = 0, lock_order_id = NULL, lock_expire_time = NULL
                    WHERE lock_order_id = ?
                    """, orderId);
            jdbc.update("DELETE FROM t_order_seat WHERE order_id = ?", orderId);
            jdbc.update("DELETE FROM t_order WHERE id = ?", orderId);
        }
    }

    @Test
    void expired_order_is_timed_out_and_seats_released() {
        Long seatId = jdbc.queryForObject("""
                SELECT id FROM t_seat WHERE hall_id = 5 AND row_num = 1 AND col_num = 1
                """, Long.class);
        jdbc.update("""
                INSERT INTO t_order (order_no, user_id, screening_id, total_amount, status, expire_time)
                VALUES (?, 1, 5, 42.90, 0, DATE_SUB(NOW(), INTERVAL 1 MINUTE))
                """, 1900000000000000099L);
        orderId = jdbc.queryForObject("""
                SELECT id FROM t_order WHERE order_no = ?
                """, Long.class, 1900000000000000099L);
        jdbc.update("""
                INSERT INTO t_order_seat (order_id, seat_id, row_num, col_num, price)
                VALUES (?, ?, 1, 1, 42.90)
                """, orderId, seatId);
        jdbc.update("""
                UPDATE t_screening_seat
                SET status = 1, lock_order_id = ?, lock_expire_time = DATE_SUB(NOW(), INTERVAL 1 MINUTE)
                WHERE screening_id = 5 AND seat_id = ?
                """, orderId, seatId);

        timeoutJob.expireOrders();

        assertEquals(3, jdbc.queryForObject("SELECT status FROM t_order WHERE id = ?", Integer.class, orderId));
        assertEquals(0, jdbc.queryForObject(
                "SELECT status FROM t_screening_seat WHERE screening_id = 5 AND seat_id = ?",
                Integer.class, seatId));
    }
}
