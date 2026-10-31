package com.cdwater.ticketbox.booking.adapter.out.support;

import com.cdwater.ticketbox.booking.application.port.OrderNoGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SnowflakeOrderNoGeneratorTest {

    @Autowired
    OrderNoGenerator orderNoGenerator;

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
