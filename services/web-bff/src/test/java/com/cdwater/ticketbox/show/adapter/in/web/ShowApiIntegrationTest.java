package com.cdwater.ticketbox.show.adapter.in.web;

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

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShowApiIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    JdbcTemplate jdbc;

    RestClient client;
    final ObjectMapper om = new ObjectMapper();

    @BeforeEach
    void setUp() {
        client = RestClient.builder().baseUrl("http://localhost:" + port).build();
        jdbc.update("UPDATE ticket_box_content.t_show SET start_time = DATE_ADD(NOW(), INTERVAL id DAY)");
    }

    private String token() {
        String raw = client.post().uri("/api/user/login/by-pwd")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("phone", "13800000001", "password", "Aa123456"))
                .retrieve().body(String.class);
        return om.readTree(raw).get("data").get("tokenValue").asString();
    }

    private JsonNode get(String url, String token) {
        String raw = client.get().uri(url)
                .header("Authorization", "Bearer " + token)
                .retrieve().body(String.class);
        return om.readTree(raw);
    }

    @Test
    void list_and_filter() {
        String token = token();
        JsonNode all = get("/api/shows?page=1&size=10", token);
        assertEquals("A200", all.get("code").asString(), all.toString());
        assertEquals(8, all.get("data").get("total").asInt());

        JsonNode filtered = get("/api/shows?page=1&size=10&keyword=星光", token);
        assertEquals(2, filtered.get("data").get("total").asInt());
        assertTrue(filtered.get("data").get("records").get(0).get("minPrice").isNumber());
    }

    @Test
    void detail_with_ticket_availability() {
        String token = token();
        JsonNode node = get("/api/shows/1", token);
        assertEquals("A200", node.get("code").asString(), node.toString());
        JsonNode data = node.get("data");
        assertEquals("星光之夜演唱会", data.get("title").asString());
        assertEquals(3, data.get("ticketTypes").size());
        assertEquals(18, data.get("ticketTypes").get(0).get("availableSeats").asInt());
    }

    @Test
    void missing_show_returns_404() {
        JsonNode node = get("/api/shows/999999", token());
        assertEquals("C404", node.get("code").asString());
    }
}
