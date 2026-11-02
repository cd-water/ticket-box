package com.cdwater.ticketbox.user.adapter.in.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * 集成测试：密码登录、登出、登录响应。
 * 前置：docker compose up -d；identity-service 与 web-bff 已启动。
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserAuthIntegrationTest {

    private static final String USER1_PHONE = "13800000001";
    private static final String PASSWORD = "Aa123456";
    private static final String PROTECTED_API = "/api/movies?tab=showing";

    @LocalServerPort
    int port;

    @Autowired
    JdbcTemplate jdbc;

    RestClient client;
    final ObjectMapper om = new ObjectMapper();

    @BeforeEach
    void setUp() {
        client = RestClient.builder().baseUrl("http://localhost:" + port).build();
    }

    private JsonNode post(String url, Map<String, String> body) {
        return post(url, body, null);
    }

    private JsonNode post(String url, Map<String, String> body, String tokenValue) {
        RestClient.RequestBodySpec spec = client.post().uri(url)
                .contentType(MediaType.APPLICATION_JSON);
        if (tokenValue != null) {
            spec.header("Authorization", "Bearer " + tokenValue);
        }
        String raw = spec.body(body).retrieve().body(String.class);
        return om.readTree(raw == null ? "{}" : raw);
    }

    private JsonNode get(String url, String tokenValue) {
        RestClient.RequestHeadersSpec<?> spec = client.get().uri(url);
        if (tokenValue != null) {
            spec.header("Authorization", "Bearer " + tokenValue);
        }
        String raw = spec.retrieve().body(String.class);
        return om.readTree(raw == null ? "{}" : raw);
    }

    private JsonNode getWithAuthorization(String url, String headerValue) {
        String raw = client.get().uri(url)
                .header("Authorization", headerValue)
                .retrieve().body(String.class);
        return om.readTree(raw == null ? "{}" : raw);
    }

    private String login(String phone, String password) {
        JsonNode node = post("/api/user/login/by-pwd",
                Map.of("phone", phone, "password", password));
        assertEquals("A200", node.get("code").asString(), node.toString());
        return node.get("data").get("tokenValue").asString();
    }

    @Test
    void login_by_pwd_seed_user1_success() {
        JsonNode node = post("/api/user/login/by-pwd",
                Map.of("phone", USER1_PHONE, "password", PASSWORD));
        assertEquals("A200", node.get("code").asString(), node.toString());
        assertFalse(node.get("data").get("tokenValue").asString().isEmpty());
        JsonNode user = node.get("data");
        assertEquals(USER1_PHONE, user.get("phone").asString());
        assertEquals("user_mjafo8", user.get("nickname").asString());
    }

    @Test
    void login_by_pwd_wrong_password() {
        JsonNode node = post("/api/user/login/by-pwd",
                Map.of("phone", USER1_PHONE, "password", "wrong"));
        assertEquals("C400", node.get("code").asString());
        assertEquals("手机号或密码错误", node.get("message").asString());
    }

    @Test
    void login_by_pwd_unknown_phone_same_error() {
        JsonNode node = post("/api/user/login/by-pwd",
                Map.of("phone", "13800009999", "password", PASSWORD));
        assertEquals("C400", node.get("code").asString());
        assertEquals("手机号或密码错误", node.get("message").asString());
    }

    @Test
    void login_by_pwd_no_password_user_same_error() {
        JsonNode node = post("/api/user/login/by-pwd",
                Map.of("phone", "13800000002", "password", PASSWORD));
        assertEquals("C400", node.get("code").asString());
        assertEquals("手机号或密码错误", node.get("message").asString());
    }

    @Test
    void protected_api_without_token_unauthorized() {
        assertEquals("C401", get(PROTECTED_API, null).get("code").asString());
    }

    @Test
    void protected_api_with_garbage_token_unauthorized() {
        assertEquals("C401", get(PROTECTED_API, "not-a-real-token").get("code").asString());
    }

    @Test
    void protected_api_with_prefixless_token_unauthorized() {
        assertEquals("C401", getWithAuthorization(PROTECTED_API, "rawtoken").get("code").asString());
    }

    @Test
    void protected_api_with_empty_bearer_token_unauthorized() {
        assertEquals("C401", getWithAuthorization(PROTECTED_API, "Bearer ").get("code").asString());
    }

    @Test
    void wrong_http_method_bad_request() {
        assertEquals("C400", get("/api/user/login/by-code", null).get("code").asString());
    }

    @Test
    void unknown_route_under_protected_prefix_not_found() {
        assertEquals("C404", get("/api/user/nonexistent", null).get("code").asString());
    }

    @Test
    void login_by_pwd_disabled_user_forbidden() {
        jdbc.update("UPDATE t_user SET status = 0 WHERE phone = ?", USER1_PHONE);
        try {
            JsonNode node = post("/api/user/login/by-pwd",
                    Map.of("phone", USER1_PHONE, "password", PASSWORD));
            assertEquals("C403", node.get("code").asString());
        } finally {
            jdbc.update("UPDATE t_user SET status = 1 WHERE phone = ?", USER1_PHONE);
        }
    }

    @Test
    void logout_invalidates_token() {
        String token = login(USER1_PHONE, PASSWORD);
        JsonNode logout = post("/api/user/logout", Map.of(), token);
        assertEquals("A200", logout.get("code").asString());
        assertEquals("C401", get(PROTECTED_API, token).get("code").asString());
    }

    @Test
    void concurrent_login_creates_distinct_valid_tokens() {
        String t1 = login(USER1_PHONE, PASSWORD);
        String t2 = login(USER1_PHONE, PASSWORD);
        assertNotEquals(t1, t2);
        assertEquals("A200", get(PROTECTED_API, t1).get("code").asString());
        assertEquals("A200", get(PROTECTED_API, t2).get("code").asString());
    }
}
