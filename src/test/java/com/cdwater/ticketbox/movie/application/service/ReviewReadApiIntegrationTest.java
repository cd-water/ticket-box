package com.cdwater.ticketbox.movie.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReviewReadApiIntegrationTest {

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

    private JsonNode get(String url, String token) {
        String raw = client.get().uri(url)
                .header("Authorization", "Bearer " + token)
                .retrieve().body(String.class);
        return om.readTree(raw);
    }

    @Test
    void hot_reviews_order_and_preview() {
        String token = token();
        JsonNode node = get("/api/movies/1/reviews?sort=hot&size=10", token);
        assertEquals("A200", node.get("code").asString(), node.toString());
        JsonNode records = node.get("data").get("records");
        assertEquals(1, records.get(0).get("id").asLong());
        assertEquals(8, records.get(0).get("likeCount").asInt());
        assertEquals(2, records.get(0).get("replyCount").asInt());
        assertEquals(2, records.get(0).get("replies").size());
        assertEquals(3, records.get(0).get("replies").get(0).get("id").asLong());
        assertEquals(3L, records.get(0).get("replies").get(1).get("replyTo").get("id").asLong());
        assertFalse(node.get("data").get("hasMore").asBoolean());
    }

    @Test
    void latest_reviews_order() {
        String token = token();
        JsonNode node = get("/api/movies/1/reviews?sort=latest&size=10", token);
        JsonNode records = node.get("data").get("records");
        assertEquals(2L, records.get(0).get("id").asLong());
        assertEquals(1L, records.get(1).get("id").asLong());
    }

    @Test
    void cursor_pagination_has_no_duplicates() {
        String token = token();
        JsonNode first = get("/api/movies/1/reviews?sort=hot&size=2", token);
        assertEquals(2, first.get("data").get("records").size());
        assertTrue(first.get("data").get("hasMore").asBoolean());
        String cursor = first.get("data").get("nextCursor").asString();

        JsonNode second = get("/api/movies/1/reviews?sort=hot&size=2&cursor=" + cursor, token);
        assertEquals("A200", second.get("code").asString(), second.toString());
        assertEquals(2, second.get("data").get("records").size());
        assertFalse(second.get("data").get("hasMore").asBoolean());
        long firstSecondPageId = second.get("data").get("records").get(0).get("id").asLong();
        assertEquals(36L, firstSecondPageId);
    }

    @Test
    void replies_are_chronological() {
        String token = token();
        JsonNode node = get("/api/reviews/1/replies?size=10", token);
        JsonNode records = node.get("data").get("records");
        assertEquals(3L, records.get(0).get("id").asLong());
        assertEquals(4L, records.get(1).get("id").asLong());
        assertEquals(3L, records.get(1).get("replyTo").get("id").asLong());
        assertFalse(node.get("data").get("hasMore").asBoolean());
    }

    @Test
    void invalid_cursor_bad_request() {
        String token = token();
        JsonNode node = get("/api/movies/1/reviews?sort=hot&cursor=not-base64!!", token);
        assertEquals("C400", node.get("code").asString());
        assertEquals("无效的分页游标", node.get("message").asString());
    }

    @Test
    void hidden_root_replies_not_found() {
        String token = token();
        JsonNode node = get("/api/reviews/7/replies", token);
        assertEquals("C404", node.get("code").asString());
    }

    @Test
    void out_of_range_time_cursor_bad_request() {
        String token = token();
        String cursor = Base64.getUrlEncoder().withoutPadding()
                .encodeToString("+10000-01-01T00:00:00|1".getBytes(StandardCharsets.UTF_8));

        JsonNode reviews = get("/api/movies/1/reviews?sort=latest&cursor=" + cursor, token);
        assertEquals("C400", reviews.get("code").asString());
        assertEquals("无效的分页游标", reviews.get("message").asString());

        JsonNode replies = get("/api/reviews/1/replies?cursor=" + cursor, token);
        assertEquals("C400", replies.get("code").asString());
    }
}
