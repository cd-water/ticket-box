package com.cdwater.ticketbox.cinema.adapter.in.web;

import com.cdwater.ticketbox.cinema.application.command.MovieOrderCommandService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestClient;
import com.cdwater.ticketbox.support.SeedScreeningFixture;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieOrderApiIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    JdbcTemplate jdbc;

    @Autowired
    MovieOrderCommandService orderCommandService;

    RestClient client;
    final ObjectMapper om = new ObjectMapper();
    final List<Long> createdOrderIds = new ArrayList<>();

    @BeforeEach
    void setUp() {
        client = RestClient.builder().baseUrl("http://localhost:" + port).build();
        SeedScreeningFixture.refresh(jdbc);
        restoreSeedLock();
    }

    @AfterEach
    void cleanUp() {
        for (Long orderNo : createdOrderIds) {
            jdbc.update("""
                    UPDATE ticket_box_inventory.t_screening_seat
                    SET status = 0, lock_order_no = NULL, lock_expire_time = NULL
                    WHERE lock_order_no = ?
                    """, orderNo);
            jdbc.update("DELETE FROM t_movie_order_seat WHERE order_no = ?", orderNo);
            jdbc.update("DELETE FROM t_movie_order WHERE order_no = ?", orderNo);
        }
        createdOrderIds.clear();
        restoreSeedLock();
    }

    private void restoreSeedLock() {
        jdbc.update("""
                UPDATE t_movie_order SET status = 0, expire_time = DATE_ADD(NOW(), INTERVAL 15 MINUTE)
                WHERE order_no = 1900000000000000001
                """);
        jdbc.update("""
                UPDATE ticket_box_inventory.t_screening_seat ss
                JOIN ticket_box_inventory.t_seat s ON s.id = ss.seat_id
                SET ss.status = 1, ss.lock_order_no = 1900000000000000001,
                    ss.lock_expire_time = DATE_ADD(NOW(), INTERVAL 15 MINUTE)
                WHERE ss.screening_id = 1 AND s.row_num = 1 AND s.col_num IN (1, 2)
                """);
    }

    private String token() {
        String raw = client.post().uri("/api/user/login/by-pwd")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("phone", "13800000001", "password", "Aa123456"))
                .retrieve().body(String.class);
        return om.readTree(raw).get("data").get("tokenValue").asString();
    }

    private JsonNode post(String url, String token, Object body) {
        String raw = client.post().uri(url)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve().body(String.class);
        return om.readTree(raw);
    }

    private JsonNode get(String url, String token) {
        String raw = client.get().uri(url)
                .header("Authorization", "Bearer " + token)
                .retrieve().body(String.class);
        return om.readTree(raw);
    }

    private List<Long> seatIds(long hallId, int row, int colStart, int colEnd) {
        return jdbc.queryForList("""
                SELECT id FROM ticket_box_inventory.t_seat
                WHERE hall_id = ? AND row_num = ? AND col_num BETWEEN ? AND ?
                ORDER BY col_num
                """, Long.class, hallId, row, colStart, colEnd);
    }

    @Test
    void create_order_and_read_detail() {
        String token = token();
        List<Long> seatIds = seatIds(2L, 2, 1, 2);
        JsonNode node = post("/api/movie-orders", token, Map.of(
                "screeningId", 2,
                "seatIds", seatIds));
        assertEquals("A200", node.get("code").asString(), node.toString());
        JsonNode data = node.get("data");
        long orderNo = Long.parseLong(data.get("orderNo").asString());
        createdOrderIds.add(orderNo);
        assertTrue(data.get("orderNo").isString());
        assertEquals(0, data.get("status").asInt());
        assertEquals(91.80, data.get("totalAmount").asDouble(), 0.001);
        assertEquals(2, data.get("seats").size());

        JsonNode detail = get("/api/movie-orders/" + orderNo, token);
        assertEquals("A200", detail.get("code").asString(), detail.toString());
        assertEquals(String.valueOf(orderNo), detail.get("data").get("orderNo").asString());
    }

    @Test
    void sold_seat_conflict() {
        String token = token();
        List<Long> seatIds = seatIds(1L, 1, 3, 3);
        JsonNode node = post("/api/movie-orders", token, Map.of(
                "screeningId", 1,
                "seatIds", seatIds));
        assertEquals("C409", node.get("code").asString());
    }

    @Test
    void invalid_seat_lists_bad_request() {
        String token = token();
        assertEquals("C400", post("/api/movie-orders", token, Map.of(
                "screeningId", 2, "seatIds", List.of())).get("code").asString());
        List<Long> one = seatIds(2L, 3, 1, 1);
        assertEquals("C400", post("/api/movie-orders", token, Map.of(
                "screeningId", 2, "seatIds", List.of(one.get(0), one.get(0)))).get("code").asString());
        List<Long> seven = seatIds(2L, 3, 1, 7);
        assertEquals("C400", post("/api/movie-orders", token, Map.of(
                "screeningId", 2, "seatIds", seven)).get("code").asString());
    }

    @Test
    void concurrent_same_seat_only_one_succeeds() throws Exception {
        String token = token();
        List<Long> seatIds = seatIds(3L, 4, 1, 1);
        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            List<Future<String>> futures = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                futures.add(pool.submit(() -> post("/api/movie-orders", token, Map.of(
                        "screeningId", 3, "seatIds", seatIds)).get("code").asString()));
            }
            List<String> codes = new ArrayList<>();
            for (Future<String> future : futures) {
                codes.add(future.get());
            }
            assertEquals(1, codes.stream().filter("A200"::equals).count(), codes.toString());
            assertEquals(1, codes.stream().filter("C409"::equals).count(), codes.toString());
        } finally {
            pool.shutdownNow();
        }
        Long orderNo = jdbc.queryForObject("""
                SELECT lock_order_no FROM ticket_box_inventory.t_screening_seat
                WHERE screening_id = 3 AND seat_id = ?
                """, Long.class, seatIds.get(0));
        assertNotNull(orderNo);
        createdOrderIds.add(orderNo);
    }

    @Test
    void timeout_does_not_release_seat_taken_by_new_order() {
        String token = token();
        List<Long> seatIds = seatIds(5L, 6, 1, 1);

        JsonNode first = post("/api/movie-orders", token, Map.of(
                "screeningId", 5, "seatIds", seatIds));
        assertEquals("A200", first.get("code").asString(), first.toString());
        long firstOrderNo = Long.parseLong(first.get("data").get("orderNo").asString());
        createdOrderIds.add(firstOrderNo);

        jdbc.update("UPDATE t_movie_order SET expire_time = DATE_SUB(NOW(), INTERVAL 1 MINUTE) WHERE order_no = ?",
                firstOrderNo);
        jdbc.update("""
                UPDATE ticket_box_inventory.t_screening_seat
                SET lock_expire_time = DATE_SUB(NOW(), INTERVAL 1 MINUTE)
                WHERE screening_id = 5 AND seat_id = ?
                """, seatIds.get(0));

        JsonNode second = post("/api/movie-orders", token, Map.of(
                "screeningId", 5, "seatIds", seatIds));
        assertEquals("A200", second.get("code").asString(), second.toString());
        long secondOrderNo = Long.parseLong(second.get("data").get("orderNo").asString());
        createdOrderIds.add(secondOrderNo);

        orderCommandService.expireOrder(firstOrderNo);

        Long lockOrderNo = jdbc.queryForObject("""
                SELECT lock_order_no FROM ticket_box_inventory.t_screening_seat
                WHERE screening_id = 5 AND seat_id = ?
                """, Long.class, seatIds.get(0));
        Integer seatStatus = jdbc.queryForObject("""
                SELECT status FROM ticket_box_inventory.t_screening_seat
                WHERE screening_id = 5 AND seat_id = ?
                """, Integer.class, seatIds.get(0));
        assertEquals(secondOrderNo, lockOrderNo);
        assertEquals(1, seatStatus);
    }

    @Test
    void timeout_releases_seats_on_order_read() {
        String token = token();
        List<Long> seatIds = seatIds(4L, 5, 1, 1);
        JsonNode created = post("/api/movie-orders", token, Map.of(
                "screeningId", 4, "seatIds", seatIds));
        assertEquals("A200", created.get("code").asString(), created.toString());
        long orderNo = Long.parseLong(created.get("data").get("orderNo").asString());
        createdOrderIds.add(orderNo);

        jdbc.update("UPDATE t_movie_order SET expire_time = DATE_SUB(NOW(), INTERVAL 1 MINUTE) WHERE order_no = ?", orderNo);
        jdbc.update("""
                UPDATE ticket_box_inventory.t_screening_seat SET lock_expire_time = DATE_SUB(NOW(), INTERVAL 1 MINUTE)
                WHERE screening_id = 4 AND seat_id = ?
                """, seatIds.get(0));

        JsonNode detail = get("/api/movie-orders/" + orderNo, token);
        assertEquals("A200", detail.get("code").asString(), detail.toString());
        assertEquals(2, detail.get("data").get("status").asInt());
        Integer seatStatus = jdbc.queryForObject("""
                SELECT status FROM ticket_box_inventory.t_screening_seat WHERE screening_id = 4 AND seat_id = ?
                """, Integer.class, seatIds.get(0));
        assertEquals(0, seatStatus);
    }

    @Test
    void list_filters_by_status_and_owner() {
        String token = token();
        for (int status = 0; status <= 2; status++) {
            long expected = jdbc.queryForObject(
                    "SELECT COUNT(*) FROM t_movie_order WHERE user_id = 1 AND status = ?",
                    Long.class, status);
            JsonNode list = get("/api/movie-orders?status=" + status + "&page=1&size=10", token);
            assertEquals("A200", list.get("code").asString(), list.toString());
            assertEquals(expected, list.get("data").get("total").asLong());
            for (JsonNode record : list.get("data").get("records")) {
                assertEquals(status, record.get("status").asInt());
                long owner = jdbc.queryForObject(
                        "SELECT user_id FROM t_movie_order WHERE order_no = ?",
                        Long.class, Long.parseLong(record.get("orderNo").asString()));
                assertEquals(1L, owner);
            }

            if (expected > 1) {
                JsonNode page2 = get("/api/movie-orders?status=" + status + "&page=2&size=1", token);
                assertEquals(expected, page2.get("data").get("total").asLong());
                assertEquals(1, page2.get("data").get("records").size());
                assertEquals(status, page2.get("data").get("records").get(0).get("status").asInt());
            }
        }
    }

    private Long createMovieOrder(String token, int screeningId, List<Long> seatIds) {
        JsonNode node = post("/api/movie-orders", token, Map.of(
                "screeningId", screeningId,
                "seatIds", seatIds));
        assertEquals("A200", node.get("code").asString(), node.toString());
        Long orderNo = Long.parseLong(node.get("data").get("orderNo").asString());
        createdOrderIds.add(orderNo);
        return orderNo;
    }

    @Test
    void pay_success_sells_seats() {
        String token = token();
        List<Long> seatIds = seatIds(2L, 3, 1, 2);
        Long orderNo = createMovieOrder(token, 2, seatIds);

        JsonNode paid = post("/api/movie-orders/" + orderNo + "/pay", token, Map.of());
        assertEquals("A200", paid.get("code").asString(), paid.toString());
        assertEquals(1, paid.get("data").get("status").asInt());
        assertEquals(2, paid.get("data").get("seats").size());
        for (Long seatId : seatIds) {
            assertEquals(2, jdbc.queryForObject(
                    "SELECT status FROM ticket_box_inventory.t_screening_seat WHERE screening_id = 2 AND seat_id = ?",
                    Integer.class, seatId));
        }
    }

    @Test
    void cancel_releases_seats() {
        String token = token();
        List<Long> seatIds = seatIds(2L, 4, 1, 2);
        Long orderNo = createMovieOrder(token, 2, seatIds);

        JsonNode cancelled = post("/api/movie-orders/" + orderNo + "/cancel", token, Map.of());
        assertEquals("A200", cancelled.get("code").asString(), cancelled.toString());
        assertEquals(2, cancelled.get("data").get("status").asInt());
        for (Long seatId : seatIds) {
            assertEquals(0, jdbc.queryForObject(
                    "SELECT status FROM ticket_box_inventory.t_screening_seat WHERE screening_id = 2 AND seat_id = ?",
                    Integer.class, seatId));
            assertNull(jdbc.queryForObject(
                    "SELECT lock_order_no FROM ticket_box_inventory.t_screening_seat WHERE screening_id = 2 AND seat_id = ?",
                    Long.class, seatId));
        }
    }

    @Test
    void pay_twice_second_conflicts() {
        String token = token();
        Long orderNo = createMovieOrder(token, 2, seatIds(2L, 5, 1, 2));
        assertEquals("A200", post("/api/movie-orders/" + orderNo + "/pay", token, Map.of())
                .get("code").asString());
        assertEquals("C409", post("/api/movie-orders/" + orderNo + "/pay", token, Map.of())
                .get("code").asString());
    }

    @Test
    void pay_expired_cancels_and_releases_seats() {
        String token = token();
        List<Long> seatIds = seatIds(2L, 6, 1, 2);
        Long orderNo = createMovieOrder(token, 2, seatIds);
        jdbc.update("UPDATE t_movie_order SET expire_time = DATE_SUB(NOW(), INTERVAL 1 MINUTE) WHERE order_no = ?", orderNo);

        JsonNode paid = post("/api/movie-orders/" + orderNo + "/pay", token, Map.of());
        assertEquals("C409", paid.get("code").asString(), paid.toString());
        assertEquals(2, jdbc.queryForObject(
                "SELECT status FROM t_movie_order WHERE order_no = ?", Integer.class, orderNo));
        for (Long seatId : seatIds) {
            assertEquals(0, jdbc.queryForObject(
                    "SELECT status FROM ticket_box_inventory.t_screening_seat WHERE screening_id = 2 AND seat_id = ?",
                    Integer.class, seatId));
        }
    }

    @Test
    void cannot_read_pay_or_cancel_others_order() {
        String token = token();
        assertEquals("C404", get("/api/movie-orders/1900000000000000006", token)
                .get("code").asString());
        assertEquals("C404", post("/api/movie-orders/1900000000000000006/pay", token, Map.of())
                .get("code").asString());
        assertEquals("C404", post("/api/movie-orders/1900000000000000006/cancel", token, Map.of())
                .get("code").asString());
    }

    @Test
    void concurrent_pay_only_one_succeeds() throws Exception {
        String token = token();
        Long orderNo = createMovieOrder(token, 2, seatIds(2L, 7, 1, 2));
        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            List<Future<String>> futures = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                futures.add(pool.submit(() -> post(
                        "/api/movie-orders/" + orderNo + "/pay", token, Map.of())
                        .get("code").asString()));
            }
            List<String> codes = new ArrayList<>();
            for (Future<String> future : futures) {
                codes.add(future.get());
            }
            assertEquals(1, codes.stream().filter("A200"::equals).count(), codes.toString());
            assertEquals(1, codes.stream().filter("C409"::equals).count(), codes.toString());
        } finally {
            pool.shutdownNow();
        }
        assertEquals(1, jdbc.queryForObject(
                "SELECT status FROM t_movie_order WHERE order_no = ?", Integer.class, orderNo));
        assertEquals(2L, jdbc.queryForObject(
                "SELECT COUNT(*) FROM ticket_box_inventory.t_screening_seat WHERE lock_order_no = ? AND status = 2",
                Long.class, orderNo));
    }

    @Test
    void pay_when_locks_released_conflicts() {
        String token = token();
        List<Long> seatIds = seatIds(2L, 8, 1, 2);
        Long orderNo = createMovieOrder(token, 2, seatIds);
        jdbc.update("""
                UPDATE ticket_box_inventory.t_screening_seat
                SET status = 0, lock_order_no = NULL, lock_expire_time = NULL
                WHERE lock_order_no = ?
                """, orderNo);

        JsonNode paid = post("/api/movie-orders/" + orderNo + "/pay", token, Map.of());
        assertEquals("C409", paid.get("code").asString(), paid.toString());
        assertEquals(0, jdbc.queryForObject(
                "SELECT status FROM t_movie_order WHERE order_no = ?", Integer.class, orderNo));
        for (Long seatId : seatIds) {
            assertEquals(0, jdbc.queryForObject(
                    "SELECT status FROM ticket_box_inventory.t_screening_seat WHERE screening_id = 2 AND seat_id = ?",
                    Integer.class, seatId));
        }
    }

    @Test
    void concurrent_pay_and_cancel_only_one_succeeds() throws Exception {
        String token = token();
        List<Long> seatIds = seatIds(2L, 9, 1, 2);
        Long orderNo = createMovieOrder(token, 2, seatIds);

        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            Future<String> pay = pool.submit(() -> post(
                    "/api/movie-orders/" + orderNo + "/pay", token, Map.of())
                    .get("code").asString());
            Future<String> cancel = pool.submit(() -> post(
                    "/api/movie-orders/" + orderNo + "/cancel", token, Map.of())
                    .get("code").asString());
            List<String> codes = List.of(pay.get(), cancel.get());
            assertEquals(1, codes.stream().filter("A200"::equals).count(), codes.toString());
            assertEquals(1, codes.stream().filter("C409"::equals).count(), codes.toString());
        } finally {
            pool.shutdownNow();
        }

        int orderStatus = jdbc.queryForObject(
                "SELECT status FROM t_movie_order WHERE order_no = ?", Integer.class, orderNo);
        int seatStatus = jdbc.queryForObject(
                "SELECT status FROM ticket_box_inventory.t_screening_seat WHERE screening_id = 2 AND seat_id = ?",
                Integer.class, seatIds.get(0));
        if (orderStatus == 1) {
            assertEquals(2, seatStatus);
        } else {
            assertEquals(2, orderStatus);
            assertEquals(0, seatStatus);
        }
    }

    @Test
    void without_token_unauthorized() {
        String raw = client.post().uri("/api/movie-orders")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("screeningId", 2, "seatIds", List.of(1)))
                .retrieve().body(String.class);
        assertEquals("C401", om.readTree(raw).get("code").asString());
    }
}
