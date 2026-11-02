package com.cdwater.ticketbox.cinema.adapter.in.schedule;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieOrderTimeoutJobTest {

    private static final long ORDER_NO = 1900000000000000099L;

    @Autowired
    JdbcTemplate jdbc;

    @Autowired
    MovieOrderTimeoutJob timeoutJob;

    Long orderNo;

    @AfterEach
    void cleanUp() {
        if (orderNo != null) {
            jdbc.update("""
                    UPDATE ticket_box_inventory.t_screening_seat
                    SET status = 0, lock_order_no = NULL, lock_expire_time = NULL
                    WHERE lock_order_no = ?
                    """, orderNo);
            jdbc.update("DELETE FROM t_movie_order_seat WHERE order_no = ?", orderNo);
            jdbc.update("DELETE FROM t_movie_order WHERE order_no = ?", orderNo);
        }
    }

    @Test
    void expired_order_is_timed_out_and_seats_released() {
        Long seatId = jdbc.queryForObject("""
                SELECT id FROM ticket_box_inventory.t_seat WHERE hall_id = 5 AND row_num = 1 AND col_num = 1
                """, Long.class);
        jdbc.update("""
                INSERT INTO t_movie_order (order_no, user_id, screening_id, movie_title, cinema_name,
                                           hall_name, start_time, end_time, total_amount, status, expire_time)
                VALUES (?, 1, 5, '星际征途', '星光国际影城（人民广场店）', '1号厅',
                        DATE_ADD(NOW(), INTERVAL 120 MINUTE), DATE_ADD(NOW(), INTERVAL 270 MINUTE),
                        42.90, 0, DATE_SUB(NOW(), INTERVAL 1 MINUTE))
                """, ORDER_NO);
        orderNo = ORDER_NO;
        jdbc.update("""
                INSERT INTO t_movie_order_seat (order_no, seat_id, row_num, col_num, price)
                VALUES (?, ?, 1, 1, 42.90)
                """, orderNo, seatId);
        jdbc.update("""
                UPDATE ticket_box_inventory.t_screening_seat
                SET status = 1, lock_order_no = ?, lock_expire_time = DATE_SUB(NOW(), INTERVAL 1 MINUTE)
                WHERE screening_id = 5 AND seat_id = ?
                """, orderNo, seatId);

        timeoutJob.expireOrders();

        assertEquals(2, jdbc.queryForObject("SELECT status FROM t_movie_order WHERE order_no = ?", Integer.class, orderNo));
        assertEquals(0, jdbc.queryForObject(
                "SELECT status FROM ticket_box_inventory.t_screening_seat WHERE screening_id = 5 AND seat_id = ?",
                Integer.class, seatId));
    }
}
