package com.cdwater.ticketbox.inventory.adapter.out.persistence;

import com.cdwater.ticketbox.inventory.domain.repository.ShowSeatInventoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
class ShowSeatInventoryDataAccessTest {

    private static final long ORDER_NO = 2900000000000000101L;

    @Autowired
    ShowSeatInventoryRepository repository;

    @AfterEach
    void cleanUp() {
        repository.releaseByOrder(ORDER_NO);
    }

    @Test
    void show1_ticket_availability() {
        var availability = repository.countAvailableByZone(1L);
        assertEquals(18L, availability.get("A区"));
        assertEquals(30L, availability.get("B区"));
        assertEquals(30L, availability.get("C区"));
    }

    @Test
    void lock_available_seats_in_seat_no_order() {
        var seats = repository.lockSeats(1L, "A区", 2, ORDER_NO,
                LocalDateTime.now().plusMinutes(5));
        assertEquals(2, seats.size());
        assertEquals(3, seats.get(0).getSeatNo());
        assertEquals(4, seats.get(1).getSeatNo());
    }
}
