package com.cdwater.ticketbox.support;

import org.springframework.jdbc.core.JdbcTemplate;

public final class SeedScreeningFixture {

    private SeedScreeningFixture() {
    }

    public static void refresh(JdbcTemplate jdbc) {
        jdbc.update("""
                UPDATE t_screening
                SET start_time = DATE_ADD(NOW(), INTERVAL (id * 30) MINUTE),
                    end_time = DATE_ADD(DATE_ADD(NOW(), INTERVAL (id * 30) MINUTE), INTERVAL 150 MINUTE)
                """);
    }
}
