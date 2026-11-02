package com.cdwater.ticketbox.cinema.adapter.in.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestClient;
import com.cdwater.ticketbox.support.SeedScreeningFixture;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SeatMapApiIntegrationTest {

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
                UPDATE ticket_box_order.t_movie_order SET status = 0, expire_time = DATE_ADD(NOW(), INTERVAL 15 MINUTE)
                WHERE order_no = 1900000000000000001
                """);
        jdbc.update("""
                UPDATE ticket_box_inventory.t_screening_seat ss
                JOIN ticket_box_inventory.t_seat s ON s.id = ss.seat_id
                SET ss.status = 1, ss.lock_order_no = 1,
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
    void seat_map_contains_rectangular_layout_and_statuses() {
        String token = token();
        JsonNode node = get("/api/screenings/1/seats", token);
        assertEquals("A200", node.get("code").asString(), node.toString());
        JsonNode data = node.get("data");
        assertEquals(10, data.get("rowCount").asInt());
        assertEquals(12, data.get("colCount").asInt());
        assertEquals(120, data.get("seats").size());
        assertEquals("星际征途", data.get("movieTitle").asString());
        assertEquals("星光国际影城（人民广场店）", data.get("cinemaName").asString());
        assertEquals("1号厅", data.get("hallName").asString());

        long available = 0, locked = 0, sold = 0;
        for (JsonNode seat : data.get("seats")) {
            int status = seat.get("status").asInt();
            if (status == 0) available++;
            else if (status == 1) locked++;
            else if (status == 2) sold++;
        }
        assertEquals(116, available);
        assertEquals(2, locked);
        assertEquals(2, sold);
    }

    @Test
    void missing_screening_not_found() {
        String token = token();
        JsonNode node = get("/api/screenings/99999/seats", token);
        assertEquals("C404", node.get("code").asString());
    }
}
