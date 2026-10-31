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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReviewLikeApiIntegrationTest {

    private static final long COMMENT_ID = 17L;
    private static final long USER_ID = 1L;

    @LocalServerPort
    int port;

    @Autowired
    JdbcTemplate jdbc;

    RestClient client;
    final ObjectMapper om = new ObjectMapper();

    @BeforeEach
    void setUp() {
        client = RestClient.builder().baseUrl("http://localhost:" + port).build();
        cleanUp();
    }

    @AfterEach
    void cleanUp() {
        jdbc.update("DELETE FROM t_movie_comment_like WHERE comment_id = ? AND user_id = ?",
                COMMENT_ID, USER_ID);
        jdbc.update("""
                UPDATE t_movie_comment c
                SET c.like_count = (SELECT COUNT(*) FROM t_movie_comment_like l WHERE l.comment_id = c.id)
                WHERE c.id = ?
                """, COMMENT_ID);
    }

    private String token() {
        String raw = client.post().uri("/api/user/login/by-pwd")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("phone", "13800000001", "password", "Aa123456"))
                .retrieve().body(String.class);
        return om.readTree(raw).get("data").get("tokenValue").asString();
    }

    private JsonNode post(String url, String token) {
        String raw = client.post().uri(url)
                .header("Authorization", "Bearer " + token)
                .retrieve().body(String.class);
        return om.readTree(raw);
    }

    @Test
    void toggle_like_and_unlike() {
        String token = token();
        JsonNode first = post("/api/reviews/" + COMMENT_ID + "/like", token);
        assertEquals("A200", first.get("code").asString(), first.toString());
        assertTrue(first.get("data").get("liked").asBoolean());
        assertEquals(4, first.get("data").get("likeCount").asInt());

        JsonNode second = post("/api/reviews/" + COMMENT_ID + "/like", token);
        assertEquals("A200", second.get("code").asString(), second.toString());
        assertFalse(second.get("data").get("liked").asBoolean());
        assertEquals(3, second.get("data").get("likeCount").asInt());
    }

    @Test
    void concurrent_same_user_toggles_do_not_deadlock() throws Exception {
        String token = token();
        ExecutorService pool = Executors.newFixedThreadPool(2);
        try {
            for (int round = 0; round < 10; round++) {
                jdbc.update("INSERT IGNORE INTO t_movie_comment_like (comment_id, user_id) VALUES (?, ?)",
                        COMMENT_ID, USER_ID);
                jdbc.update("""
                        UPDATE t_movie_comment c
                        SET c.like_count = (SELECT COUNT(*) FROM t_movie_comment_like l WHERE l.comment_id = c.id)
                        WHERE c.id = ?
                        """, COMMENT_ID);

                List<Future<String>> futures = new ArrayList<>();
                for (int i = 0; i < 2; i++) {
                    futures.add(pool.submit(() -> post(
                            "/api/reviews/" + COMMENT_ID + "/like", token).get("code").asString()));
                }
                for (Future<String> future : futures) {
                    assertEquals("A200", future.get(), "same-user concurrent toggle must not deadlock");
                }

                Integer likeCount = jdbc.queryForObject(
                        "SELECT like_count FROM t_movie_comment WHERE id = ?", Integer.class, COMMENT_ID);
                Integer rowCount = jdbc.queryForObject(
                        "SELECT COUNT(*) FROM t_movie_comment_like WHERE comment_id = ?",
                        Integer.class, COMMENT_ID);
                assertEquals(rowCount, likeCount);

                jdbc.update("DELETE FROM t_movie_comment_like WHERE comment_id = ? AND user_id = ?",
                        COMMENT_ID, USER_ID);
                jdbc.update("""
                        UPDATE t_movie_comment c
                        SET c.like_count = (SELECT COUNT(*) FROM t_movie_comment_like l WHERE l.comment_id = c.id)
                        WHERE c.id = ?
                        """, COMMENT_ID);
            }
        } finally {
            pool.shutdownNow();
        }
    }

    @Test
    void like_hidden_review_not_found() {
        String token = token();
        JsonNode node = post("/api/reviews/7/like", token);
        assertEquals("C404", node.get("code").asString());
    }

    @Test
    void without_token_unauthorized() {
        String raw = client.post().uri("/api/reviews/" + COMMENT_ID + "/like")
                .retrieve().body(String.class);
        assertEquals("C401", om.readTree(raw).get("code").asString());
    }
}
