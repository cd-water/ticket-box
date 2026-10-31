package com.cdwater.ticketbox.movie.application.support;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.ResultCode;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReviewCursorTest {

    @Test
    void hot_cursor_round_trip() {
        String cursor = ReviewCursor.encode("8", 1L);
        ReviewCursor.Cursor decoded = ReviewCursor.decode(cursor);
        assertEquals("8", decoded.value());
        assertEquals(1L, decoded.id());
        assertEquals(8, ReviewCursor.parseInt(decoded.value()));
    }

    @Test
    void time_cursor_round_trip_keeps_colons() {
        LocalDateTime time = LocalDateTime.of(2026, 10, 31, 10, 0, 0);
        String cursor = ReviewCursor.encode(time.toString(), 42L);
        ReviewCursor.Cursor decoded = ReviewCursor.decode(cursor);
        assertEquals(time, ReviewCursor.parseTime(decoded.value()));
        assertEquals(42L, decoded.id());
    }

    @Test
    void invalid_cursor_is_bad_request() {
        BizException e = assertThrows(BizException.class,
                () -> ReviewCursor.decode("not-base64!!"));
        assertEquals(ResultCode.BAD_REQUEST.getCode(), e.getCode());
        assertEquals("无效的分页游标", e.getMessage());
    }

    @Test
    void invalid_number_and_time_are_bad_request() {
        assertEquals(ResultCode.BAD_REQUEST.getCode(),
                assertThrows(BizException.class, () -> ReviewCursor.parseInt("abc")).getCode());
        assertEquals(ResultCode.BAD_REQUEST.getCode(),
                assertThrows(BizException.class, () -> ReviewCursor.parseTime("abc")).getCode());
    }

    @Test
    void time_cursor_outside_mysql_datetime_range_is_bad_request() {
        assertEquals(ResultCode.BAD_REQUEST.getCode(),
                assertThrows(BizException.class,
                        () -> ReviewCursor.parseTime("+10000-01-01T00:00:00")).getCode());
        assertEquals(ResultCode.BAD_REQUEST.getCode(),
                assertThrows(BizException.class,
                        () -> ReviewCursor.parseTime("-1000-01-01T00:00:00")).getCode());
        assertEquals(ResultCode.BAD_REQUEST.getCode(),
                assertThrows(BizException.class,
                        () -> ReviewCursor.parseTime("0999-12-31T23:59:59")).getCode());
        assertEquals(LocalDateTime.of(1000, 1, 1, 0, 0),
                ReviewCursor.parseTime("1000-01-01T00:00:00"));
    }
}
