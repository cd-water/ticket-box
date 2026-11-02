package com.cdwater.ticketbox.play.adapter.in.web;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlayOrderPayApiIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    JdbcTemplate jdbc;

    RestClient client;
    final ObjectMapper om = new ObjectMapper();
    final List<Long> created = new ArrayList<>();
    final Map<Long, Integer> stockBefore = new HashMap<>();

    @BeforeEach
    void setUp() {
        client = RestClient.builder().baseUrl("http://localhost:" + port).build();
    }

    @AfterEach
    void cleanUp() {
        for (Long orderNo : created) {
            jdbc.update("DELETE FROM t_play_order WHERE order_no = ?", orderNo);
        }
        created.clear();
        stockBefore.forEach((itemId, stock) ->
                jdbc.update("UPDATE t_play_item SET stock = ? WHERE id = ?", stock, itemId));
        stockBefore.clear();
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

    private int stock(long itemId) {
        return jdbc.queryForObject("SELECT stock FROM t_play_item WHERE id = ?", Integer.class, itemId);
    }

    private Long create(String token, long itemId, int quantity) {
        stockBefore.putIfAbsent(itemId, stock(itemId));
        JsonNode node = post("/api/play-orders", token,
                Map.of("itemId", itemId, "quantity", quantity));
        assertEquals("A200", node.get("code").asString(), node.toString());
        Long orderNo = Long.parseLong(node.get("data").get("orderNo").asString());
        created.add(orderNo);
        return orderNo;
    }

    @Test
    void pay_success_keeps_stock() {
        String token = token();
        int before = stock(1);
        Long orderNo = create(token, 1, 1);
        JsonNode paid = post("/api/play-orders/" + orderNo + "/pay", token, Map.of());
        assertEquals("A200", paid.get("code").asString(), paid.toString());
        assertEquals(1, paid.get("data").get("status").asInt());
        assertEquals(before - 1, stock(1));
    }

    @Test
    void pay_twice_second_conflicts() {
        String token = token();
        Long orderNo = create(token, 1, 1);
        assertEquals("A200", post("/api/play-orders/" + orderNo + "/pay", token, Map.of())
                .get("code").asString());
        JsonNode second = post("/api/play-orders/" + orderNo + "/pay", token, Map.of());
        assertEquals("C409", second.get("code").asString());
    }

    @Test
    void pay_expired_cancels_and_restores_stock() {
        String token = token();
        int before = stock(1);
        stockBefore.put(1L, before);
        Long orderNo = create(token, 1, 1);
        jdbc.update("UPDATE t_play_order SET expire_time = DATE_SUB(NOW(), INTERVAL 1 MINUTE) WHERE order_no = ?", orderNo);

        JsonNode paid = post("/api/play-orders/" + orderNo + "/pay", token, Map.of());
        assertEquals("C409", paid.get("code").asString(), paid.toString());
        assertEquals(2, jdbc.queryForObject(
                "SELECT status FROM t_play_order WHERE order_no = ?", Integer.class, orderNo));
        assertEquals(before, stock(1));
    }

    @Test
    void concurrent_pay_only_one_succeeds() throws Exception {
        String token = token();
        Long orderNo = create(token, 1, 1);
        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            List<Future<String>> futures = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                futures.add(pool.submit(() ->
                        post("/api/play-orders/" + orderNo + "/pay", token, Map.of())
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
    void concurrent_create_last_stock_only_one_succeeds() throws Exception {
        String token = token();
        int original = stock(6);
        stockBefore.put(6L, original);
        jdbc.update("UPDATE t_play_item SET stock = 1 WHERE id = 6");

        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            List<Future<JsonNode>> futures = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                futures.add(pool.submit(() -> post("/api/play-orders", token,
                        Map.of("itemId", 6, "quantity", 1))));
            }
            List<JsonNode> nodes = new ArrayList<>();
            for (Future<JsonNode> future : futures) {
                nodes.add(future.get());
            }
            long ok = nodes.stream().filter(n -> "A200".equals(n.get("code").asString())).count();
            long conflict = nodes.stream().filter(n -> "C409".equals(n.get("code").asString())).count();
            assertEquals(1, ok, nodes.toString());
            assertEquals(1, conflict, nodes.toString());
            nodes.stream()
                    .filter(n -> "A200".equals(n.get("code").asString()))
                    .forEach(n -> created.add(Long.parseLong(n.get("data").get("orderNo").asString())));
            assertEquals(0, stock(6));
        } finally {
            pool.shutdownNow();
        }
    }
}
