package com.cdwater.ticketbox.cinema.adapter.in.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestClient;
import com.cdwater.ticketbox.support.SeedScreeningFixture;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CinemaApiIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    JdbcTemplate jdbc;

    RestClient client;
    final ObjectMapper om = new ObjectMapper();

    @BeforeEach
    void setUp() {
        client = RestClient.builder().baseUrl("http://localhost:" + port).build();
        SeedScreeningFixture.refresh(jdbc);
        restoreSeedLock();
    }

    private void restoreSeedLock() {
        jdbc.update("""
                UPDATE t_order SET status = 0, expire_time = DATE_ADD(NOW(), INTERVAL 15 MINUTE)
                WHERE id = 1
                """);
        jdbc.update("""
                UPDATE t_screening_seat ss
                JOIN t_seat s ON s.id = ss.seat_id
                SET ss.status = 1, ss.lock_order_id = 1,
                    ss.lock_expire_time = DATE_ADD(NOW(), INTERVAL 15 MINUTE)
                WHERE ss.screening_id = 1 AND s.row_num = 1 AND s.col_num IN (1, 2)
                """);
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
    void cinema_list_and_name_filter() {
        String token = token();
        JsonNode all = get("/api/cinemas?page=1&size=10", token);
        assertEquals("A200", all.get("code").asString(), all.toString());
        assertEquals(12, all.get("data").get("total").asInt());
        assertEquals(10, all.get("data").get("records").size());

        JsonNode filtered = get("/api/cinemas?page=1&size=10&name=星光", token);
        assertEquals(1, filtered.get("data").get("total").asInt());
        assertTrue(filtered.get("data").get("records").get(0).get("name").asString().contains("星光"));
    }

    @Test
    void cinema_list_filtered_by_movie() {
        String token = token();

        JsonNode movie1 = get("/api/cinemas?page=1&size=10&movieId=1", token);
        assertEquals("A200", movie1.get("code").asString(), movie1.toString());
        assertEquals(2, movie1.get("data").get("total").asInt());
        String names = movie1.get("data").get("records").toString();
        assertTrue(names.contains("星光"));
        assertTrue(names.contains("金逸"));

        JsonNode named = get("/api/cinemas?page=1&size=10&movieId=1&name=星光", token);
        assertEquals(1, named.get("data").get("total").asInt());

        JsonNode upcoming = get("/api/cinemas?page=1&size=10&movieId=3", token);
        assertEquals(0, upcoming.get("data").get("total").asInt());

        JsonNode invalid = get("/api/cinemas?page=1&size=10&movieId=0", token);
        assertEquals("C400", invalid.get("code").asString());

        JsonNode missing = get("/api/cinemas?page=1&size=10&movieId=999999", token);
        assertEquals(0, missing.get("data").get("total").asInt());
    }

    @Test
    void cinema_detail_contains_movies() {
        String token = token();
        JsonNode node = get("/api/cinemas/1", token);
        assertEquals("A200", node.get("code").asString(), node.toString());
        assertEquals(2, node.get("data").get("movies").size());
        assertTrue(node.get("data").get("movies").toString().contains("星际征途"));
        assertTrue(node.get("data").get("movies").toString().contains("山海奇缘"));
    }

    @Test
    void future_screenings_and_available_seats() {
        String token = token();
        JsonNode node = get("/api/cinemas/1/movies/1/screenings", token);
        assertEquals("A200", node.get("code").asString(), node.toString());
        JsonNode records = node.get("data");
        assertEquals(2, records.size());
        assertEquals(1L, records.get(0).get("id").asLong());
        assertEquals("1号厅", records.get(0).get("hallName").asString());
        assertEquals(116, records.get(0).get("availableSeats").asInt());
        assertEquals(120, records.get(1).get("availableSeats").asInt());
    }

    @Test
    void cinema_detail_excludes_off_shelf_canceled_and_past() {
        String token = token();
        jdbc.update("DELETE FROM t_screening WHERE id IN (9001, 9002, 9003)");
        jdbc.update("""
                INSERT INTO t_screening
                    (id, cinema_id, hall_id, movie_id, start_time, end_time, price, status)
                VALUES
                    (9001, 1, 1, 5, DATE_ADD(NOW(), INTERVAL 1 HOUR),
                     DATE_ADD(NOW(), INTERVAL 3 HOUR), 30.00, 1),
                    (9002, 1, 1, 16, DATE_SUB(NOW(), INTERVAL 3 HOUR),
                     DATE_SUB(NOW(), INTERVAL 1 HOUR), 30.00, 1),
                    (9003, 1, 1, 17, DATE_ADD(NOW(), INTERVAL 1 HOUR),
                     DATE_ADD(NOW(), INTERVAL 3 HOUR), 30.00, 0)
                """);
        try {
            JsonNode node = get("/api/cinemas/1", token);
            assertEquals("A200", node.get("code").asString(), node.toString());
            assertEquals(2, node.get("data").get("movies").size());
            String movies = node.get("data").get("movies").toString();
            assertFalse(movies.contains("旧日时光"));
            assertFalse(movies.contains("云端之城"));
            assertFalse(movies.contains("逆风飞翔"));
        } finally {
            jdbc.update("DELETE FROM t_screening WHERE id IN (9001, 9002, 9003)");
        }
    }

    @Test
    void without_token_unauthorized() {
        String raw = client.get().uri("/api/cinemas?page=1&size=10").retrieve().body(String.class);
        assertEquals("C401", om.readTree(raw).get("code").asString());
    }
}
