package com.cdwater.ticketbox.show.adapter.in.web;

import com.cdwater.ticketbox.show.adapter.in.schedule.ShowOrderTimeoutJob;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShowOrderApiIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    JdbcTemplate jdbc;

    @Autowired
    ShowOrderTimeoutJob timeoutJob;

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

    private JsonNode get(String url, String token) {
        String raw = client.get().uri(url)
                .header("Authorization", "Bearer " + token)
                .retrieve().body(String.class);
        return om.readTree(raw);
    }

    private JsonNode createOrder(String token) {
        JsonNode node = post("/api/show-orders", token,
                Map.of("showId", 1, "ticketTypeId", 1, "quantity", 2));
        assertEquals("A200", node.get("code").asString(), node.toString());
        Long orderNo = Long.parseLong(node.get("data").get("orderNo").asString());
        created.add(orderNo);
        return node;
    }

    @Test
    void create_writes_snapshots_and_no_seats_before_pay() {
        JsonNode data = createOrder(token()).get("data");
        assertEquals(0, data.get("status").asInt());
        assertEquals("星光之夜演唱会", data.get("showTitle").asString());
        assertEquals("A区", data.get("zone").asString());
        assertEquals(1360.00, data.get("totalAmount").asDouble(), 0.001);
        assertEquals(2, data.get("quantity").asInt());
        assertEquals(0, data.get("seats").size());
    }

    @Test
    void create_rejects_insufficient_quantity() {
        String token = token();
        JsonNode node = post("/api/show-orders", token,
                Map.of("showId", 1, "ticketTypeId", 1, "quantity", 7));
        assertEquals("C400", node.get("code").asString());
    }

    @Test
    void cancel_once_then_conflict() {
        String token = token();
        JsonNode createdNode = createOrder(token);
        Long orderNo = Long.parseLong(createdNode.get("data").get("orderNo").asString());

        JsonNode cancelled = post("/api/show-orders/" + orderNo + "/cancel", token, Map.of());
        assertEquals("A200", cancelled.get("code").asString(), cancelled.toString());
        assertEquals(2, cancelled.get("data").get("status").asInt());

        JsonNode again = post("/api/show-orders/" + orderNo + "/cancel", token, Map.of());
        assertEquals("C409", again.get("code").asString());
    }

    @Test
    void list_filters_by_status() {
        String token = token();
        createOrder(token);
        for (int status = 0; status <= 2; status++) {
            long expected = jdbc.queryForObject(
                    "SELECT COUNT(*) FROM t_show_order WHERE user_id = 1 AND status = ?",
                    Long.class, status);
            JsonNode list = get("/api/show-orders?status=" + status + "&page=1&size=10", token);
            assertEquals("A200", list.get("code").asString(), list.toString());
            assertEquals(expected, list.get("data").get("total").asLong());
        }
    }

    @Test
    void expired_order_detail_returns_cancelled_status() {
        String token = token();
        JsonNode createdNode = createOrder(token);
        Long orderNo = Long.parseLong(createdNode.get("data").get("orderNo").asString());
        jdbc.update("UPDATE t_show_order SET expire_time = DATE_SUB(NOW(), INTERVAL 1 MINUTE) WHERE order_no = ?", orderNo);

        JsonNode detail = get("/api/show-orders/" + orderNo, token);
        assertEquals("A200", detail.get("code").asString(), detail.toString());
        assertEquals(2, detail.get("data").get("status").asInt());
    }

    @Test
    void invalid_status_returns_bad_request() {
        JsonNode node = get("/api/show-orders?status=3&page=1&size=10", token());
        assertEquals("C400", node.get("code").asString(), node.toString());
    }

    @Test
    void cannot_read_others_order() {
        JsonNode node = get("/api/show-orders/1900000000000000013", token());
        assertEquals("C404", node.get("code").asString());
    }

    @Test
    void timeout_job_cancels_expired_order() {
        String token = token();
        JsonNode createdNode = createOrder(token);
        Long orderNo = Long.parseLong(createdNode.get("data").get("orderNo").asString());
        jdbc.update("UPDATE t_show_order SET expire_time = DATE_SUB(NOW(), INTERVAL 1 MINUTE) WHERE order_no = ?", orderNo);

        timeoutJob.expireOrders();

        assertEquals(2, jdbc.queryForObject(
                "SELECT status FROM t_show_order WHERE order_no = ?", Integer.class, orderNo));
    }
}
