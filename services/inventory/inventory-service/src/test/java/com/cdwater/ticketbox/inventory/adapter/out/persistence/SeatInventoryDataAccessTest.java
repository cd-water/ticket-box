package com.cdwater.ticketbox.inventory.adapter.out.persistence;

import com.cdwater.ticketbox.inventory.domain.repository.SeatInventoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
class SeatInventoryDataAccessTest {

    @Autowired
    SeatInventoryRepository repository;

    @Test
    void seat_map_statuses() {
        var seats = repository.findSeatMap(1L);
        assertEquals(120, seats.size());
        assertEquals(116, seats.stream().filter(s -> s.getStatus() == 0).count());
        assertEquals(2, seats.stream().filter(s -> s.getStatus() == 1).count());
        assertEquals(2, seats.stream().filter(s -> s.getStatus() == 2).count());
    }

    @Test
    void available_counts() {
        var counts = repository.countAvailable(List.of(1L, 25L));
        assertEquals(116L, counts.get(1L));
        assertEquals(120L, counts.get(25L));
    }
}
