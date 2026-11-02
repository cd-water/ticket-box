package com.cdwater.ticketbox.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DatabaseTimeZoneTest {

    @Autowired
    JdbcTemplate jdbc;

    @Test
    void database_now_matches_jvm_local_time() {
        assertEquals("Asia/Shanghai", TimeZone.getDefault().getID());
        LocalDateTime dbNow = jdbc.queryForObject("SELECT NOW()", LocalDateTime.class);
        long minutes = Math.abs(Duration.between(dbNow, LocalDateTime.now()).toMinutes());
        assertTrue(minutes < 1, "database NOW() differs from JVM LocalDateTime.now() by " + minutes + " minutes");
    }
}
