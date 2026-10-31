package com.cdwater.ticketbox.movie.application.service;

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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReviewWriteApiIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    JdbcTemplate jdbc;

    RestClient client;
    final ObjectMapper om = new ObjectMapper();
    final List<Long> createdIds = new ArrayList<>();

    @BeforeEach
    void setUp() {
        client = RestClient.builder().baseUrl("http://localhost:" + port).build();
    }

    @AfterEach
    void cleanUp() {
        if (!createdIds.isEmpty()) {
            jdbc.update("DELETE FROM t_movie_comment WHERE id IN (" +
                    String.join(",", createdIds.stream().map(String::valueOf).toList()) + ")");
        }
        jdbc.update("""
                UPDATE t_movie_comment c
                LEFT JOIN (
                    SELECT root_id, COUNT(*) AS cnt
                    FROM t_movie_comment
                    WHERE root_id <> 0 AND status = 1
                    GROUP BY root_id
                ) r ON r.root_id = c.id
                SET c.reply_count = COALESCE(r.cnt, 0)
                WHERE c.root_id = 0
                """);
        createdIds.clear();
    }

    private String token() {
        String raw = client.post().uri("/api/user/login/by-pwd")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("phone", "13800000001", "password", "Aa123456"))
                .retrieve().body(String.class);
        return om.readTree(raw).get("data").get("tokenValue").asString();
    }

    private JsonNode post(String url, String token, Map<String, String> body) {
        String raw = client.post().uri(url)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve().body(String.class);
        return om.readTree(raw);
    }

    @Test
    void create_root_review() {
        String token = token();
        JsonNode node = post("/api/movies/1/reviews", token, Map.of("content", "测试影评-根"));
        assertEquals("A200", node.get("code").asString(), node.toString());
        long id = node.get("data").get("id").asLong();
        createdIds.add(id);
        assertEquals("测试影评-根", node.get("data").get("content").asString());
        assertEquals(0, node.get("data").get("replyCount").asInt());
    }

    @Test
    void reply_to_root_sets_root_and_parent() {
        String token = token();
        JsonNode node = post("/api/reviews/1/reply", token, Map.of("content", "测试影评-回复根"));
        assertEquals("A200", node.get("code").asString(), node.toString());
        long id = node.get("data").get("id").asLong();
        createdIds.add(id);
        assertEquals(1L, node.get("data").get("rootId").asLong());
        assertEquals(1L, node.get("data").get("parentId").asLong());
        assertEquals(1L, node.get("data").get("replyTo").get("id").asLong());
    }

    @Test
    void reply_to_reply_keeps_direct_parent() {
        String token = token();
        JsonNode node = post("/api/reviews/3/reply", token, Map.of("content", "测试影评-回复回复"));
        assertEquals("A200", node.get("code").asString(), node.toString());
        long id = node.get("data").get("id").asLong();
        createdIds.add(id);
        assertEquals(1L, node.get("data").get("rootId").asLong());
        assertEquals(3L, node.get("data").get("parentId").asLong());
    }

    @Test
    void hidden_reply_target_not_found() {
        String token = token();
        JsonNode node = post("/api/reviews/7/reply", token, Map.of("content", "不应该写入"));
        assertEquals("C404", node.get("code").asString());
    }

    @Test
    void blank_and_overlong_content_bad_request() {
        String token = token();
        assertEquals("C400", post("/api/movies/1/reviews", token, Map.of("content", "   "))
                .get("code").asString());
        assertEquals("C400", post("/api/movies/1/reviews", token, Map.of("content", "a".repeat(1001)))
                .get("code").asString());
    }
}
