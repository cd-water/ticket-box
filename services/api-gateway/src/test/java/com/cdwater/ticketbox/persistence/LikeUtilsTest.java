package com.cdwater.ticketbox.persistence;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LikeUtilsTest {

    @Test
    void escapes_wildcards_and_backslash() {
        assertEquals("100\\%\\_done", LikeUtils.escape("100%_done"));
        assertEquals("a\\\\b", LikeUtils.escape("a\\b"));
    }

    @Test
    void plain_text_is_unchanged() {
        assertEquals("星光影城", LikeUtils.escape("星光影城"));
    }
}
