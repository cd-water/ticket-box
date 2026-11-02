package com.cdwater.ticketbox.movie.adapter.in.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 前置：docker compose up -d，Flyway 已应用 V1/V2。
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieApiIntegrationTest {

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
        JsonNode node = om.readTree(raw);
        assertEquals("A200", node.get("code").asString(), node.toString());
        return node.get("data").get("tokenValue").asString();
    }

    private JsonNode get(String url, String token) {
        String raw = client.get().uri(url)
                .header("Authorization", "Bearer " + token)
                .retrieve().body(String.class);
        return om.readTree(raw);
    }

    @Test
    void hot_list_and_pagination() {
        String token = token();
        JsonNode node = get("/api/movies?tab=showing&page=1&size=5", token);
        assertEquals("A200", node.get("code").asString(), node.toString());
        JsonNode data = node.get("data");
        assertEquals(15, data.get("total").asInt());
        assertEquals(5, data.get("records").size());
        assertEquals(1, data.get("page").asInt());
        assertEquals("城市脉搏", data.get("records").get(0).get("title").asString());
        assertTrue(data.get("records").get(0).has("poster"));
        assertTrue(data.get("records").get(0).has("releaseDate"));
    }

    @Test
    void upcoming_list_and_search_scope() {
        String token = token();
        JsonNode upcoming = get("/api/movies?tab=upcoming&page=1&size=12", token);
        assertEquals(15, upcoming.get("data").get("total").asInt());
        assertTrue(upcoming.get("data").get("records").toString().contains("午夜列车"));

        JsonNode showingHit = get("/api/movies?tab=showing&keyword=星际", token);
        assertEquals(1, showingHit.get("data").get("total").asInt());

        JsonNode upcomingMiss = get("/api/movies?tab=upcoming&keyword=星际", token);
        assertEquals(0, upcomingMiss.get("data").get("total").asInt());
    }

    @Test
    void percent_keyword_is_literal() {
        String token = token();
        JsonNode node = get("/api/movies?tab=showing&keyword=%25", token);
        assertEquals(0, node.get("data").get("total").asInt());
    }

    @Test
    void movie_detail_with_genres_and_persons() {
        String token = token();
        JsonNode node = get("/api/movies/1", token);
        assertEquals("A200", node.get("code").asString(), node.toString());
        JsonNode data = node.get("data");
        assertEquals("星际征途", data.get("title").asString());
        assertEquals("showing", data.get("tab").asString());
        assertEquals(9, data.get("reviewCount").asInt());
        assertTrue(data.get("genres").toString().contains("科幻"));
        assertTrue(data.get("directors").toString().contains("张远航"));
        assertTrue(data.get("actors").toString().contains("陆衡"));
    }

    @Test
    void off_shelf_movie_not_found() {
        String token = token();
        JsonNode node = get("/api/movies/5", token);
        assertEquals("C404", node.get("code").asString());
    }

    @Test
    void without_token_unauthorized() {
        String raw = client.get().uri("/api/movies?tab=showing").retrieve().body(String.class);
        assertEquals("C401", om.readTree(raw).get("code").asString());
    }
}
