package com.cdwater.ticketbox.inventory.adapter.out.persistence;

import com.cdwater.ticketbox.inventory.domain.repository.ShowSeatInventoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ShowSeatInventoryDataAccessTest {

    @Autowired
    ShowSeatInventoryRepository repository;

    @Test
    void show1_ticket_availability() {
        var availability = repository.countAvailableByZone(1L);
        assertEquals(18L, availability.get("A区"));
        assertEquals(30L, availability.get("B区"));
        assertEquals(30L, availability.get("C区"));
    }

    @Test
    void lock_available_seats_in_seat_no_order() {
        var seats = repository.lockAvailableSeats(1L, "A区");
        assertEquals(18, seats.size());
        assertEquals(3, seats.get(0).getSeatNo());
        assertEquals(4, seats.get(1).getSeatNo());
    }
}
