package com.cdwater.ticketbox.play.adapter.in.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlayApiIntegrationTest {

    @LocalServerPort
    int port;

    RestClient client;
    final ObjectMapper om = new ObjectMapper();

    @BeforeEach
    void setUp() {
        client = RestClient.builder().baseUrl("http://localhost:" + port).build();
    }

    private String token() {
        String raw = client.post().uri("/api/user/login/by-pwd")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("phone", "13800000001", "password", "Aa123456"))
                .retrieve().body(String.class);
        return om.readTree(raw).get("data").get("tokenValue").asString();
    }

    private JsonNode get(String url) {
        String raw = client.get().uri(url)
                .header("Authorization", "Bearer " + token())
                .retrieve().body(String.class);
        return om.readTree(raw);
    }

    @Test
    void list_and_filter() {
        JsonNode all = get("/api/plays?page=1&size=10");
        assertEquals("A200", all.get("code").asString(), all.toString());
        assertEquals(6, all.get("data").get("total").asInt());

        JsonNode filtered = get("/api/plays?page=1&size=10&keyword=温泉");
        assertEquals(1, filtered.get("data").get("total").asInt());
    }

    @Test
    void detail_contains_price_and_stock() {
        JsonNode node = get("/api/plays/1");
        assertEquals("A200", node.get("code").asString(), node.toString());
        assertEquals("城市密室逃脱", node.get("data").get("title").asString());
        assertEquals(189.00, node.get("data").get("price").asDouble(), 0.001);
        assertTrue(node.get("data").get("stock").asInt() > 0);
    }

    @Test
    void missing_play_returns_404() {
        assertEquals("C404", get("/api/plays/999999").get("code").asString());
    }
}
