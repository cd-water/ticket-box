package com.cdwater.ticketbox.config;

import cn.dev33.satoken.config.SaTokenConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SaTokenConfigTest {

    @Test
    void user_config_matches_spec() {
        SaTokenConfig c = SaTokenSetup.USER.getConfig();
        assertEquals("user", SaTokenSetup.USER.getLoginType());
        assertEquals("Authorization", c.getTokenName());
        assertEquals("Bearer", c.getTokenPrefix());
        assertEquals(5184000L, c.getTimeout());
        assertEquals(2592000L, c.getActiveTimeout());
        assertEquals("uuid", c.getTokenStyle());
        assertTrue(c.getIsReadHeader());
        assertFalse(c.getIsReadCookie());
        assertFalse(c.getIsReadBody());
        assertTrue(c.getIsConcurrent());
        assertFalse(c.getIsShare());
        assertTrue(c.getAutoRenew());
        assertTrue(c.getIsLog());
    }

    @Test
    void admin_config_matches_spec() {
        SaTokenConfig c = SaTokenSetup.ADMIN.getConfig();
        assertEquals("admin", SaTokenSetup.ADMIN.getLoginType());
        assertEquals("Authorization", c.getTokenName());
        assertEquals("Bearer", c.getTokenPrefix());
        assertEquals(1296000L, c.getTimeout());
        assertEquals(604800L, c.getActiveTimeout());
        assertEquals("random-64", c.getTokenStyle());
        assertTrue(c.getIsReadHeader());
        assertFalse(c.getIsReadCookie());
        assertFalse(c.getIsReadBody());
        assertTrue(c.getIsConcurrent());
        assertFalse(c.getIsShare());
        assertTrue(c.getAutoRenew());
        assertTrue(c.getIsLog());
    }

    @Test
    void two_logics_use_independent_config_instances() {
        assertNotSame(SaTokenSetup.USER.getConfig(), SaTokenSetup.ADMIN.getConfig());
    }
}
