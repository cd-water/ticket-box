package com.cdwater.ticketbox.play.adapter.in.web;

import com.cdwater.ticketbox.play.adapter.in.schedule.PlayOrderTimeoutJob;
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
class PlayOrderApiIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    JdbcTemplate jdbc;

    @Autowired
    PlayOrderTimeoutJob timeoutJob;

    RestClient client;
    final ObjectMapper om = new ObjectMapper();
    final List<Long> created = new ArrayList<>();
    final List<Long> itemsToDelete = new ArrayList<>();
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
        for (Long itemId : itemsToDelete) {
            jdbc.update("DELETE FROM t_play_item WHERE id = ?", itemId);
        }
        itemsToDelete.clear();
    }

    private void insertItem(long id) {
        jdbc.update("DELETE FROM t_play_item WHERE id = ?", id);
        jdbc.update("""
                INSERT INTO t_play_item (id, title, cover, address, description, price, stock, status)
                VALUES (?, '测试项目', '', '测试地址', '测试项目', 10.00, 5, 1)
                """, id);
        itemsToDelete.add(id);
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
    void create_deducts_stock_and_writes_snapshot() {
        String token = token();
        int before = stock(1);
        JsonNode node = post("/api/play-orders", token, Map.of("itemId", 1, "quantity", 2));
        assertEquals("A200", node.get("code").asString(), node.toString());
        created.add(Long.parseLong(node.get("data").get("orderNo").asString()));
        stockBefore.put(1L, before);

        JsonNode data = node.get("data");
        assertEquals("城市密室逃脱", data.get("itemTitle").asString());
        assertEquals("上海市静安区南京西路100号", data.get("itemAddress").asString());
        assertEquals(189.00, data.get("unitPrice").asDouble(), 0.001);
        assertEquals(2, data.get("quantity").asInt());
        assertEquals(378.00, data.get("totalAmount").asDouble(), 0.001);
        assertEquals(before - 2, stock(1));
    }

    @Test
    void quantity_above_limit_returns_bad_request() {
        JsonNode node = post("/api/play-orders", token(), Map.of("itemId", 1, "quantity", 11));
        assertEquals("C400", node.get("code").asString());
    }

    @Test
    void insufficient_stock_returns_conflict() {
        int before = stock(3);
        stockBefore.put(3L, before);
        jdbc.update("UPDATE t_play_item SET stock = 1 WHERE id = 3");
        JsonNode node = post("/api/play-orders", token(), Map.of("itemId", 3, "quantity", 2));
        assertEquals("C409", node.get("code").asString(), node.toString());
        assertEquals(1, stock(3));
    }

    @Test
    void cancel_restores_stock_once() {
        String token = token();
        int before = stock(2);
        stockBefore.put(2L, before);
        Long orderNo = create(token, 2, 1);
        assertEquals(before - 1, stock(2));

        JsonNode cancelled = post("/api/play-orders/" + orderNo + "/cancel", token, Map.of());
        assertEquals("A200", cancelled.get("code").asString(), cancelled.toString());
        assertEquals(2, cancelled.get("data").get("status").asInt());
        assertEquals(before, stock(2));

        JsonNode again = post("/api/play-orders/" + orderNo + "/cancel", token, Map.of());
        assertEquals("C409", again.get("code").asString());
        assertEquals(before, stock(2));
    }

    @Test
    void list_filters_by_status() {
        String token = token();
        create(token, 1, 1);
        for (int status = 0; status <= 2; status++) {
            long expected = jdbc.queryForObject(
                    "SELECT COUNT(*) FROM t_play_order WHERE user_id = 1 AND status = ?",
                    Long.class, status);
            JsonNode list = get("/api/play-orders?status=" + status + "&page=1&size=10", token);
            assertEquals("A200", list.get("code").asString(), list.toString());
            assertEquals(expected, list.get("data").get("total").asLong());
        }
    }

    @Test
    void cannot_read_pay_or_cancel_others_order() {
        long foreignOrderNo = 1900000000000000911L;
        jdbc.update("DELETE FROM t_play_order WHERE order_no = ?", foreignOrderNo);
        jdbc.update("""
                INSERT INTO t_play_order (order_no, user_id, item_id, item_title, item_address,
                                          unit_price, quantity, total_amount, status, expire_time)
                VALUES (?, 2, 1, '城市密室逃脱', '上海市静安区南京西路100号',
                        189.00, 1, 189.00, 0, DATE_ADD(NOW(), INTERVAL 30 MINUTE))
                """, foreignOrderNo);
        created.add(foreignOrderNo);

        String token = token();
        assertEquals("C404", get("/api/play-orders/" + foreignOrderNo, token).get("code").asString());
        assertEquals("C404", post("/api/play-orders/" + foreignOrderNo + "/pay", token, Map.of())
                .get("code").asString());
        assertEquals("C404", post("/api/play-orders/" + foreignOrderNo + "/cancel", token, Map.of())
                .get("code").asString());
    }

    @Test
    void off_sale_item_is_hidden_and_not_purchasable() {
        String token = token();
        int originalStatus = jdbc.queryForObject(
                "SELECT status FROM t_play_item WHERE id = 5", Integer.class);
        jdbc.update("UPDATE t_play_item SET status = 0 WHERE id = 5");
        try {
            long onSale = jdbc.queryForObject(
                    "SELECT COUNT(*) FROM t_play_item WHERE status = 1", Long.class);
            JsonNode list = get("/api/plays?page=1&size=10", token);
            assertEquals(onSale, list.get("data").get("total").asLong());
            assertEquals("C404", get("/api/plays/5", token).get("code").asString());
            assertEquals("C404", post("/api/play-orders", token,
                    Map.of("itemId", 5, "quantity", 1)).get("code").asString());
        } finally {
            jdbc.update("UPDATE t_play_item SET status = ? WHERE id = 5", originalStatus);
        }
    }

    @Test
    void timeout_job_restores_stock() {
        long itemId = 9901L;
        insertItem(itemId);
        String token = token();
        Long orderNo = create(token, itemId, 2);
        assertEquals(3, stock(itemId));
        jdbc.update("UPDATE t_play_order SET expire_time = DATE_SUB(NOW(), INTERVAL 1 MINUTE) WHERE order_no = ?", orderNo);

        timeoutJob.expireOrders();

        assertEquals(2, jdbc.queryForObject(
                "SELECT status FROM t_play_order WHERE order_no = ?", Integer.class, orderNo));
        assertEquals(5, stock(itemId));
    }

    @Test
    void cancel_and_timeout_restore_stock_once() throws Exception {
        long itemId = 9902L;
        insertItem(itemId);
        String token = token();
        Long orderNo = create(token, itemId, 1);
        assertEquals(4, stock(itemId));
        jdbc.update("UPDATE t_play_order SET expire_time = DATE_SUB(NOW(), INTERVAL 1 MINUTE) WHERE order_no = ?", orderNo);

        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            Future<String> cancel = pool.submit(() -> post(
                    "/api/play-orders/" + orderNo + "/cancel", token, Map.of()).get("code").asString());
            Future<?> job = pool.submit(timeoutJob::expireOrders);
            job.get();
            String cancelCode = cancel.get();
            assertTrue("A200".equals(cancelCode) || "C409".equals(cancelCode), cancelCode);
        } finally {
            pool.shutdownNow();
        }

        assertEquals(2, jdbc.queryForObject(
                "SELECT status FROM t_play_order WHERE order_no = ?", Integer.class, orderNo));
        assertEquals(5, stock(itemId));
    }

    @Test
    void expired_detail_returns_cancelled_and_restores_stock() {
        String token = token();
        int before = stock(3);
        stockBefore.put(3L, before);
        Long orderNo = create(token, 3, 1);
        jdbc.update("UPDATE t_play_order SET expire_time = DATE_SUB(NOW(), INTERVAL 1 MINUTE) WHERE order_no = ?", orderNo);

        JsonNode detail = get("/api/play-orders/" + orderNo, token);
        assertEquals("A200", detail.get("code").asString(), detail.toString());
        assertEquals(2, detail.get("data").get("status").asInt());
        assertEquals(before, stock(3));
    }

    @Test
    void invalid_status_returns_bad_request() {
        JsonNode node = get("/api/play-orders?status=3&page=1&size=10", token());
        assertEquals("C400", node.get("code").asString());
    }
}
