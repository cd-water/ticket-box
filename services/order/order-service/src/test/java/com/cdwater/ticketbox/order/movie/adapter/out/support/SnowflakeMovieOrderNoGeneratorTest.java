package com.cdwater.ticketbox.order.movie.adapter.out.support;

import com.cdwater.ticketbox.order.movie.application.port.MovieOrderNoGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SnowflakeMovieOrderNoGeneratorTest {

    @Autowired
    MovieOrderNoGenerator orderNoGenerator;

    @Test
    void generates_positive_unique_ids() {
        Set<Long> ids = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            long id = orderNoGenerator.nextOrderNo();
            assertTrue(id > 0);
            ids.add(id);
        }
        assertEquals(1000, ids.size());
    }
}
