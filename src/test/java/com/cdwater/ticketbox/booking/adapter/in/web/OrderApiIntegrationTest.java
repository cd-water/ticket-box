package com.cdwater.ticketbox.booking.adapter.in.web;

import com.cdwater.ticketbox.booking.application.command.OrderCommandService;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderApiIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    JdbcTemplate jdbc;

    @Autowired
    OrderCommandService orderCommandService;

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
                    UPDATE t_screening_seat
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
                UPDATE t_screening_seat ss
                JOIN t_seat s ON s.id = ss.seat_id
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
                SELECT id FROM t_seat
                WHERE hall_id = ? AND row_num = ? AND col_num BETWEEN ? AND ?
                ORDER BY col_num
                """, Long.class, hallId, row, colStart, colEnd);
    }

    @Test
    void create_order_and_read_detail() {
        String token = token();
        List<Long> seatIds = seatIds(2L, 2, 1, 2);
        JsonNode node = post("/api/orders", token, Map.of(
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

        JsonNode detail = get("/api/orders/" + orderNo, token);
        assertEquals("A200", detail.get("code").asString(), detail.toString());
        assertEquals(String.valueOf(orderNo), detail.get("data").get("orderNo").asString());
    }

    @Test
    void sold_seat_conflict() {
        String token = token();
        List<Long> seatIds = seatIds(1L, 1, 3, 3);
        JsonNode node = post("/api/orders", token, Map.of(
                "screeningId", 1,
                "seatIds", seatIds));
        assertEquals("C409", node.get("code").asString());
    }

    @Test
    void invalid_seat_lists_bad_request() {
        String token = token();
        assertEquals("C400", post("/api/orders", token, Map.of(
                "screeningId", 2, "seatIds", List.of())).get("code").asString());
        List<Long> one = seatIds(2L, 3, 1, 1);
        assertEquals("C400", post("/api/orders", token, Map.of(
                "screeningId", 2, "seatIds", List.of(one.get(0), one.get(0)))).get("code").asString());
        List<Long> seven = seatIds(2L, 3, 1, 7);
        assertEquals("C400", post("/api/orders", token, Map.of(
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
                futures.add(pool.submit(() -> post("/api/orders", token, Map.of(
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
                SELECT lock_order_no FROM t_screening_seat
                WHERE screening_id = 3 AND seat_id = ?
                """, Long.class, seatIds.get(0));
        assertNotNull(orderNo);
        createdOrderIds.add(orderNo);
    }

    @Test
    void timeout_does_not_release_seat_taken_by_new_order() {
        String token = token();
        List<Long> seatIds = seatIds(5L, 6, 1, 1);

        JsonNode first = post("/api/orders", token, Map.of(
                "screeningId", 5, "seatIds", seatIds));
        assertEquals("A200", first.get("code").asString(), first.toString());
        long firstOrderNo = Long.parseLong(first.get("data").get("orderNo").asString());
        createdOrderIds.add(firstOrderNo);

        jdbc.update("UPDATE t_movie_order SET expire_time = DATE_SUB(NOW(), INTERVAL 1 MINUTE) WHERE order_no = ?",
                firstOrderNo);
        jdbc.update("""
                UPDATE t_screening_seat
                SET lock_expire_time = DATE_SUB(NOW(), INTERVAL 1 MINUTE)
                WHERE screening_id = 5 AND seat_id = ?
                """, seatIds.get(0));

        JsonNode second = post("/api/orders", token, Map.of(
                "screeningId", 5, "seatIds", seatIds));
        assertEquals("A200", second.get("code").asString(), second.toString());
        long secondOrderNo = Long.parseLong(second.get("data").get("orderNo").asString());
        createdOrderIds.add(secondOrderNo);

        orderCommandService.expireOrder(firstOrderNo);

        Long lockOrderNo = jdbc.queryForObject("""
                SELECT lock_order_no FROM t_screening_seat
                WHERE screening_id = 5 AND seat_id = ?
                """, Long.class, seatIds.get(0));
        Integer seatStatus = jdbc.queryForObject("""
                SELECT status FROM t_screening_seat
                WHERE screening_id = 5 AND seat_id = ?
                """, Integer.class, seatIds.get(0));
        assertEquals(secondOrderNo, lockOrderNo);
        assertEquals(1, seatStatus);
    }

    @Test
    void timeout_releases_seats_on_order_read() {
        String token = token();
        List<Long> seatIds = seatIds(4L, 5, 1, 1);
        JsonNode created = post("/api/orders", token, Map.of(
                "screeningId", 4, "seatIds", seatIds));
        assertEquals("A200", created.get("code").asString(), created.toString());
        long orderNo = Long.parseLong(created.get("data").get("orderNo").asString());
        createdOrderIds.add(orderNo);

        jdbc.update("UPDATE t_movie_order SET expire_time = DATE_SUB(NOW(), INTERVAL 1 MINUTE) WHERE order_no = ?", orderNo);
        jdbc.update("""
                UPDATE t_screening_seat SET lock_expire_time = DATE_SUB(NOW(), INTERVAL 1 MINUTE)
                WHERE screening_id = 4 AND seat_id = ?
                """, seatIds.get(0));

        JsonNode detail = get("/api/orders/" + orderNo, token);
        assertEquals("A200", detail.get("code").asString(), detail.toString());
        assertEquals(3, detail.get("data").get("status").asInt());
        Integer seatStatus = jdbc.queryForObject("""
                SELECT status FROM t_screening_seat WHERE screening_id = 4 AND seat_id = ?
                """, Integer.class, seatIds.get(0));
        assertEquals(0, seatStatus);
    }

    @Test
    void without_token_unauthorized() {
        String raw = client.post().uri("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("screeningId", 2, "seatIds", List.of(1)))
                .retrieve().body(String.class);
        assertEquals("C401", om.readTree(raw).get("code").asString());
    }
}
