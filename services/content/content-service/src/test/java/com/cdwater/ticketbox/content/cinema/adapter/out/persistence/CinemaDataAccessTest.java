package com.cdwater.ticketbox.content.cinema.adapter.out.persistence;

import com.cdwater.ticketbox.content.cinema.domain.model.Cinema;
import com.cdwater.ticketbox.content.cinema.domain.repository.CinemaRepository;
import com.cdwater.ticketbox.content.cinema.domain.repository.ScreeningRepository;
import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.content.support.SeedScreeningFixture;
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
    JdbcTemplate jdbc;

    @BeforeEach
    void refreshSeedScreenings() {
        SeedScreeningFixture.refresh(jdbc);
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
        assertEquals(5, movie1.getTotal());
        assertEquals(java.util.Set.of(1L, 3L, 6L, 8L, 11L),
                movie1.getRecords().stream().map(Cinema::getId).collect(java.util.stream.Collectors.toSet()));

        PageResult<Cinema> movie1Named = cinemaRepository.page("星光", 1L, 1, 10);
        assertEquals(1, movie1Named.getTotal());

        PageResult<Cinema> upcoming = cinemaRepository.page("", 3L, 1, 10);
        assertEquals(0, upcoming.getTotal());

        PageResult<Cinema> page2 = cinemaRepository.page("", 1L, 2, 1);
        assertEquals(5, page2.getTotal());
        assertEquals(1, page2.getRecords().size());
    }

    @Test
    void cinema1_future_movies_and_screenings() {
        assertEquals(List.of(1L, 2L, 6L, 7L, 8L, 9L), screeningRepository.findFutureMovieIds(1L)
                .stream().sorted().toList());

        var screenings = screeningRepository.findFutureWithHall(1L, 1L);
        assertEquals(3, screenings.size());
        assertEquals(1L, screenings.get(0).getId());
        assertEquals("1号厅", screenings.get(0).getHallName());
    }

}
