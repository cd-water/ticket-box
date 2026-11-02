package com.cdwater.ticketbox.play.adapter.out.persistence;

import com.cdwater.ticketbox.play.domain.model.PlayItem;
import com.cdwater.ticketbox.play.domain.repository.PlayRepository;
import com.cdwater.ticketbox.common.result.PageResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlayDataAccessTest {

    @Autowired
    PlayRepository playRepository;

    @Test
    void list_and_filter() {
        PageResult<PlayItem> page = playRepository.page("", 1, 10);
        assertEquals(6, page.getTotal());
        assertEquals(1, playRepository.page("温泉", 1, 10).getTotal());
    }
}
