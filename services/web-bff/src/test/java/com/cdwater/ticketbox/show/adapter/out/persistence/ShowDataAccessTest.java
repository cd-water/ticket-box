package com.cdwater.ticketbox.show.adapter.out.persistence;

import com.cdwater.ticketbox.show.domain.model.Show;
import com.cdwater.ticketbox.show.domain.model.ShowTicketType;
import com.cdwater.ticketbox.show.domain.repository.ShowRepository;
import com.cdwater.ticketbox.show.domain.repository.ShowSeatInventoryRepository;
import com.cdwater.ticketbox.common.result.PageResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShowDataAccessTest {

    @Autowired
    ShowRepository showRepository;

    @Autowired
    ShowSeatInventoryRepository seatInventoryRepository;

    @Autowired
    JdbcTemplate jdbc;

    @BeforeEach
    void refreshShowTimes() {
        jdbc.update("UPDATE t_show SET start_time = DATE_ADD(NOW(), INTERVAL id DAY)");
    }

    @Test
    void list_only_upcoming_on_sale_shows() {
        PageResult<Show> page = showRepository.page("", 1, 10);
        assertEquals(8, page.getTotal());
        PageResult<Show> filtered = showRepository.page("星光", 1, 10);
        assertEquals(2, filtered.getTotal());
    }

    @Test
    void show1_ticket_types_and_availability() {
        Show show = showRepository.findById(1L).orElseThrow();
        assertEquals("星光之夜演唱会", show.getTitle());

        List<ShowTicketType> types = showRepository.findTicketTypes(1L);
        assertEquals(3, types.size());

        var availability = showRepository.countAvailableSeatsByZone(1L);
        assertEquals(18L, availability.get("A区"));
        assertEquals(30L, availability.get("B区"));
        assertEquals(30L, availability.get("C区"));

        assertEquals(new BigDecimal("280.00"), showRepository.findMinPrices(List.of(1L)).get(1L));
    }

    @Test
    void lock_available_seats_in_seat_no_order() {
        var seats = seatInventoryRepository.lockAvailableSeats(1L, "A区");
        assertEquals(18, seats.size());
        assertEquals(3, seats.get(0).getSeatNo());
        assertEquals(4, seats.get(1).getSeatNo());
    }

    @Test
    void past_show_is_excluded_from_list() {
        jdbc.update("UPDATE t_show SET start_time = DATE_SUB(NOW(), INTERVAL 1 DAY) WHERE id = 8");
        try {
            assertEquals(7, showRepository.page("", 1, 10).getTotal());
        } finally {
            jdbc.update("UPDATE t_show SET start_time = DATE_ADD(NOW(), INTERVAL 8 DAY) WHERE id = 8");
        }
    }
}
