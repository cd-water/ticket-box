package com.cdwater.ticketbox.cinema.infrastructure.persistence;

import com.cdwater.ticketbox.cinema.domain.model.Cinema;
import com.cdwater.ticketbox.cinema.domain.model.SeatSnapshot;
import com.cdwater.ticketbox.cinema.domain.repository.CinemaRepository;
import com.cdwater.ticketbox.cinema.domain.repository.ScreeningRepository;
import com.cdwater.ticketbox.cinema.domain.repository.ScreeningSeatRepository;
import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.support.SeedScreeningFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CinemaDataAccessTest {

    @Autowired
    CinemaRepository cinemaRepository;

    @Autowired
    ScreeningRepository screeningRepository;

    @Autowired
    ScreeningSeatRepository screeningSeatRepository;

    @Autowired
    JdbcTemplate jdbc;

    @BeforeEach
    void restoreSeedLock() {
        SeedScreeningFixture.refresh(jdbc);
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

    @Test
    void cinema_page_and_name_filter() {
        PageResult<Cinema> all = cinemaRepository.page("", null, 1, 10);
        assertEquals(12, all.getTotal());
        assertEquals(10, all.getRecords().size());

        PageResult<Cinema> filtered = cinemaRepository.page("星光", null, 1, 10);
        assertEquals(1, filtered.getTotal());
        assertEquals("星光国际影城（人民广场店）", filtered.getRecords().get(0).getName());
    }

    @Test
    void cinema_page_filters_by_movie_with_future_screenings() {
        PageResult<Cinema> movie1 = cinemaRepository.page("", 1L, 1, 10);
        assertEquals(2, movie1.getTotal());
        assertEquals(java.util.Set.of(1L, 8L),
                movie1.getRecords().stream().map(Cinema::getId).collect(java.util.stream.Collectors.toSet()));

        PageResult<Cinema> movie1Named = cinemaRepository.page("星光", 1L, 1, 10);
        assertEquals(1, movie1Named.getTotal());

        PageResult<Cinema> upcoming = cinemaRepository.page("", 3L, 1, 10);
        assertEquals(0, upcoming.getTotal());

        PageResult<Cinema> page2 = cinemaRepository.page("", 1L, 2, 1);
        assertEquals(2, page2.getTotal());
        assertEquals(1, page2.getRecords().size());
    }

    @Test
    void cinema1_future_movies_and_screenings() {
        assertEquals(List.of(1L, 2L), screeningRepository.findFutureMovieIds(1L)
                .stream().sorted().toList());

        var screenings = screeningRepository.findFutureWithHall(1L, 1L);
        assertEquals(2, screenings.size());
        assertEquals(1L, screenings.get(0).getId());
        assertEquals("1号厅", screenings.get(0).getHallName());
    }

    @Test
    void available_seat_counts() {
        Map<Long, Long> counts = screeningRepository.countAvailableSeats(List.of(1L, 25L));
        assertEquals(116L, counts.get(1L));
        assertEquals(120L, counts.get(25L));
    }

    @Test
    void seat_map_statuses() {
        List<SeatSnapshot> seats = screeningSeatRepository.findSeatMap(1L);
        assertEquals(120, seats.size());
        assertEquals(116, seats.stream().filter(s -> s.getStatus() == 0).count());
        assertEquals(2, seats.stream().filter(s -> s.getStatus() == 1).count());
        assertEquals(2, seats.stream().filter(s -> s.getStatus() == 2).count());
        assertTrue(seats.stream().anyMatch(s -> s.getRowNum() == 1 && s.getColNum() == 1
                && s.getStatus() == 1));
    }
}
