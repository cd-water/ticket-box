package com.cdwater.ticketbox.show.adapter.in.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShowOrderPayApiIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    JdbcTemplate jdbc;

    RestClient client;
    final ObjectMapper om = new ObjectMapper();
    final List<Long> created = new ArrayList<>();

    @BeforeEach
    void setUp() {
        client = RestClient.builder().baseUrl("http://localhost:" + port).build();
        jdbc.update("UPDATE ticket_box_content.t_show SET start_time = DATE_ADD(NOW(), INTERVAL id DAY)");
    }

    @AfterEach
    void cleanUp() {
        for (Long orderNo : created) {
            jdbc.update("UPDATE ticket_box_inventory.t_show_seat SET status = 0, lock_order_no = NULL WHERE lock_order_no = ?", orderNo);
            jdbc.update("DELETE FROM t_show_order_seat WHERE order_no = ?", orderNo);
            jdbc.update("DELETE FROM t_show_order WHERE order_no = ?", orderNo);
        }
        created.clear();
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

    private Long create(String token, long showId, long ticketTypeId, int quantity) {
        JsonNode node = post("/api/show-orders", token, Map.of(
                "showId", showId, "ticketTypeId", ticketTypeId, "quantity", quantity));
        assertEquals("A200", node.get("code").asString(), node.toString());
        Long orderNo = Long.parseLong(node.get("data").get("orderNo").asString());
        created.add(orderNo);
        return orderNo;
    }

    @Test
    void pay_assigns_contiguous_seats() {
        String token = token();
        Long orderNo = create(token, 1, 1, 2);

        JsonNode paid = post("/api/show-orders/" + orderNo + "/pay", token, Map.of());
        assertEquals("A200", paid.get("code").asString(), paid.toString());
        assertEquals(1, paid.get("data").get("status").asInt());
        assertEquals(2, paid.get("data").get("seats").size());
        assertEquals(3, paid.get("data").get("seats").get(0).get("seatNo").asInt());
        assertEquals(4, paid.get("data").get("seats").get(1).get("seatNo").asInt());
        assertEquals(16L, jdbc.queryForObject(
                "SELECT COUNT(*) FROM ticket_box_inventory.t_show_seat WHERE show_id = 1 AND zone = 'A区' AND status = 0",
                Long.class));
    }

    @Test
    void pay_insufficient_rolls_back_and_keeps_pending() {
        String token = token();
        Long orderNo = create(token, 2, 4, 2);
        jdbc.update("""
                UPDATE ticket_box_inventory.t_show_seat SET status = 2
                WHERE show_id = 2 AND zone = 'A区' AND seat_no > 1 AND status = 0
                """);
        try {
            JsonNode paid = post("/api/show-orders/" + orderNo + "/pay", token, Map.of());
            assertEquals("C409", paid.get("code").asString(), paid.toString());
            assertEquals(0, jdbc.queryForObject(
                    "SELECT status FROM t_show_order WHERE order_no = ?", Integer.class, orderNo));
        } finally {
            jdbc.update("""
                    UPDATE ticket_box_inventory.t_show_seat SET status = 0
                    WHERE show_id = 2 AND zone = 'A区' AND status = 2 AND lock_order_no IS NULL
                    """);
        }
    }

    @Test
    void pay_twice_second_conflicts() {
        String token = token();
        Long orderNo = create(token, 1, 1, 1);
        assertEquals("A200", post("/api/show-orders/" + orderNo + "/pay", token, Map.of())
                .get("code").asString());
        JsonNode second = post("/api/show-orders/" + orderNo + "/pay", token, Map.of());
        assertEquals("C409", second.get("code").asString());
    }

    @Test
    void concurrent_pay_only_one_succeeds() throws Exception {
        String token = token();
        Long orderNo = create(token, 1, 1, 2);
        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            List<Future<String>> futures = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                futures.add(pool.submit(() ->
                        post("/api/show-orders/" + orderNo + "/pay", token, Map.of())
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
    }

    @Test
    void pay_expired_order_cancels_it() {
        String token = token();
        Long orderNo = create(token, 1, 1, 1);
        jdbc.update("UPDATE t_show_order SET expire_time = DATE_SUB(NOW(), INTERVAL 1 MINUTE) WHERE order_no = ?", orderNo);

        JsonNode paid = post("/api/show-orders/" + orderNo + "/pay", token, Map.of());
        assertEquals("C409", paid.get("code").asString());
        assertEquals(2, jdbc.queryForObject(
                "SELECT status FROM t_show_order WHERE order_no = ?", Integer.class, orderNo));
    }
}
