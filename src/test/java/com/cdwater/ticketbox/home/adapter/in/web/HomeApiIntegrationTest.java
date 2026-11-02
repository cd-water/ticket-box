package com.cdwater.ticketbox.home.adapter.in.web;

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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HomeApiIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    JdbcTemplate jdbc;

    RestClient client;
    final ObjectMapper om = new ObjectMapper();
    final Map<Long, LocalDateTime> originalShowTimes = new HashMap<>();

    @BeforeEach
    void setUp() {
        client = RestClient.builder().baseUrl("http://localhost:" + port).build();
        jdbc.query("SELECT id, start_time FROM t_show", rs -> {
            originalShowTimes.put(rs.getLong("id"),
                    rs.getTimestamp("start_time").toLocalDateTime());
        });
        jdbc.update("UPDATE t_show SET start_time = DATE_ADD(NOW(), INTERVAL id DAY)");
    }

    @AfterEach
    void restoreShowTimes() {
        originalShowTimes.forEach((id, time) ->
                jdbc.update("UPDATE t_show SET start_time = ? WHERE id = ?", time, id));
        originalShowTimes.clear();
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
    void home_returns_banners_and_three_lists() {
        JsonNode node = get("/api/home");
        assertEquals("A200", node.get("code").asString(), node.toString());
        JsonNode data = node.get("data");

        JsonNode banners = data.get("banners");
        assertEquals(5, banners.size());
        for (int i = 0; i < banners.size(); i++) {
            assertEquals(i + 1, banners.get(i).get("id").asInt());
        }

        LocalDateTime now = LocalDateTime.now();
        assertEquals(8, data.get("movies").size());
        LocalDateTime previousRelease = null;
        for (JsonNode movie : data.get("movies")) {
            LocalDateTime release = LocalDateTime.parse(movie.get("releaseDate").asString());
            assertFalse(release.isAfter(now), "movie must be released: " + release);
            if (previousRelease != null) {
                assertFalse(release.isAfter(previousRelease), "movies must be release-desc");
            }
            previousRelease = release;
        }

        assertEquals(8, data.get("shows").size());
        LocalDateTime previousStart = null;
        for (JsonNode show : data.get("shows")) {
            LocalDateTime start = LocalDateTime.parse(show.get("startTime").asString());
            assertTrue(start.isAfter(now), "show must be upcoming: " + start);
            if (previousStart != null) {
                assertFalse(start.isBefore(previousStart), "shows must be start-asc");
            }
            previousStart = start;
            assertTrue(show.get("minPrice").isNumber());
        }

        assertEquals(6, data.get("plays").size());
        long previousId = Long.MAX_VALUE;
        for (JsonNode play : data.get("plays")) {
            long id = play.get("id").asLong();
            assertTrue(id < previousId, "plays must be id-desc");
            previousId = id;
        }
        assertEquals(6, data.get("plays").get(0).get("id").asInt());
    }

    @Test
    void empty_module_does_not_break_home() {
        List<Long> enabled = jdbc.queryForList(
                "SELECT id FROM t_play_item WHERE status = 1", Long.class);
        enabled.forEach(id -> jdbc.update("UPDATE t_play_item SET status = 0 WHERE id = ?", id));
        try {
            JsonNode node = get("/api/home");
            assertEquals("A200", node.get("code").asString(), node.toString());
            assertEquals(0, node.get("data").get("plays").size());
            assertEquals(8, node.get("data").get("movies").size());
        } finally {
            enabled.forEach(id -> jdbc.update("UPDATE t_play_item SET status = 1 WHERE id = ?", id));
        }
    }

    @Test
    void home_requires_login() {
        String raw = client.get().uri("/api/home").retrieve().body(String.class);
        assertEquals("C401", om.readTree(raw).get("code").asString());
    }
}
