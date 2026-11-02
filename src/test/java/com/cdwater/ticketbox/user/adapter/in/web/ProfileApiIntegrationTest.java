package com.cdwater.ticketbox.user.adapter.in.web;

import com.cdwater.ticketbox.config.MinioProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestClient;
import software.amazon.awssdk.services.s3.S3Client;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProfileApiIntegrationTest {

    private static final String PASSWORD_HASH =
            "$2a$10$GIo5IeU8h/hoUoXFt6sFOu91qMy.0QrjdS6zO0jcC9znCQ.5cIvpC";
    private static final String PNG_BASE64 =
            "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8BQDwAEhQGAhKmMIQAAAABJRU5ErkJggg==";
    private static final String TEST_PHONE = "13900009901";

    @LocalServerPort
    int port;

    @Autowired
    JdbcTemplate jdbc;

    @Autowired
    MinioProperties minioProperties;

    @Autowired
    S3Client s3Client;

    RestClient client;
    final ObjectMapper om = new ObjectMapper();

    @BeforeEach
    void setUp() {
        client = RestClient.builder().baseUrl("http://localhost:" + port).build();
    }

    private String login(String phone, String password) {
        String raw = client.post().uri("/api/user/login/by-pwd")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("phone", phone, "password", password))
                .retrieve().body(String.class);
        JsonNode node = om.readTree(raw);
        assertEquals("A200", node.get("code").asString(), node.toString());
        return node.get("data").get("tokenValue").asString();
    }

    private String token() {
        return login("13800000001", "Aa123456");
    }

    private JsonNode get(String url, String token) {
        String raw = client.get().uri(url)
                .header("Authorization", "Bearer " + token)
                .retrieve().body(String.class);
        return om.readTree(raw);
    }

    private JsonNode post(String url, String token, Object body) {
        String raw = client.post().uri(url)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve().body(String.class);
        return om.readTree(raw);
    }

    private JsonNode upload(String token, byte[] content, String filename, MediaType type) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", content).filename(filename).contentType(type);
        String raw = client.post().uri("/api/user/profile/avatar")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(builder.build())
                .retrieve().body(String.class);
        return om.readTree(raw);
    }

    private String nickname() {
        return jdbc.queryForObject(
                "SELECT nickname FROM t_user WHERE id = 1", String.class);
    }

    private void assumeMinio() {
        boolean available = false;
        URI endpoint = URI.create(minioProperties.getEndpoint());
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(endpoint.getHost(), endpoint.getPort()), 500);
            available = true;
        } catch (Exception ignored) {
            // MinIO 未启动时跳过上传用例
        }
        assumeTrue(available, "MinIO not available");
    }

    private int httpStatus(String url) {
        try {
            return HttpClient.newHttpClient()
                    .send(HttpRequest.newBuilder(URI.create(url)).GET().build(),
                            HttpResponse.BodyHandlers.discarding())
                    .statusCode();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteObject(String url) {
        String base = minioProperties.getPublicBaseUrl();
        if (base.endsWith("/")) {
            base = base.substring(0, base.length() - 1);
        }
        String prefix = base + "/" + minioProperties.getBucket() + "/";
        if (!url.startsWith(prefix)) {
            return;
        }
        String key = url.substring(prefix.length());
        try {
            s3Client.deleteObject(builder -> builder
                    .bucket(minioProperties.getBucket())
                    .key(key));
        } catch (Exception ignored) {
            // 清理失败不影响测试结果
        }
    }

    @Test
    void profile_returns_current_user() {
        JsonNode node = get("/api/user/profile", token());
        assertEquals("A200", node.get("code").asString(), node.toString());
        assertEquals("13800000001", node.get("data").get("phone").asString());
        assertFalse(node.get("data").get("nickname").asString().isBlank());
        assertTrue(node.get("data").get("avatar").isString());
    }

    @Test
    void update_nickname_and_validate() {
        String original = nickname();
        try {
            JsonNode updated = post("/api/user/profile/nickname", token(),
                    Map.of("nickname", "新昵称"));
            assertEquals("A200", updated.get("code").asString(), updated.toString());
            assertEquals("新昵称", updated.get("data").get("nickname").asString());
            assertEquals("新昵称", get("/api/user/profile", token())
                    .get("data").get("nickname").asString());

            assertEquals("C400", post("/api/user/profile/nickname", token(),
                    Map.of("nickname", "a")).get("code").asString());
            assertEquals("C400", post("/api/user/profile/nickname", token(),
                    Map.of("nickname", "123456789012345678901")).get("code").asString());
        } finally {
            jdbc.update("UPDATE t_user SET nickname = ? WHERE id = 1", original);
        }
    }

    @Test
    void change_password_invalidates_all_tokens() {
        String t1 = token();
        String t2 = token();
        try {
            JsonNode changed = post("/api/user/profile/password", t1,
                    Map.of("password", "NewPass123"));
            assertEquals("A200", changed.get("code").asString(), changed.toString());

            assertEquals("C401", get("/api/user/profile", t1).get("code").asString());
            assertEquals("C401", get("/api/user/profile", t2).get("code").asString());
            assertNotNull(login("13800000001", "NewPass123"));
        } finally {
            jdbc.update("UPDATE t_user SET password = ? WHERE id = 1", PASSWORD_HASH);
        }
    }

    @Test
    void upload_avatar_replaces_old_object() {
        assumeMinio();
        jdbc.update("DELETE FROM t_user WHERE phone = ?", TEST_PHONE);
        jdbc.update("""
                INSERT INTO t_user (phone, password, nickname, avatar, status)
                VALUES (?, ?, '测试用户', '', 1)
                """, TEST_PHONE, PASSWORD_HASH);
        Long userId = jdbc.queryForObject(
                "SELECT id FROM t_user WHERE phone = ?", Long.class, TEST_PHONE);
        String token = login(TEST_PHONE, "Aa123456");
        List<String> uploaded = new ArrayList<>();
        try {
            byte[] png = Base64.getDecoder().decode(PNG_BASE64);
            JsonNode first = upload(token, png, "a.png", MediaType.IMAGE_PNG);
            assertEquals("A200", first.get("code").asString(), first.toString());
            String url1 = first.get("data").get("avatar").asString();
            uploaded.add(url1);
            assertTrue(url1.contains("/ticket-box/avatars/" + userId + "/"), url1);

            JsonNode second = upload(token, png, "b.png", MediaType.IMAGE_PNG);
            assertEquals("A200", second.get("code").asString(), second.toString());
            String url2 = second.get("data").get("avatar").asString();
            uploaded.add(url2);

            assertEquals(404, httpStatus(url1));
            assertEquals(200, httpStatus(url2));
            assertEquals(url2, get("/api/user/profile", token)
                    .get("data").get("avatar").asString());
        } finally {
            uploaded.forEach(this::deleteObject);
            jdbc.update("DELETE FROM t_user WHERE phone = ?", TEST_PHONE);
        }
    }

    @Test
    void upload_rejects_empty_oversize_and_fake_image() {
        assertEquals("C400", upload(token(), new byte[0], "empty.png", MediaType.IMAGE_PNG)
                .get("code").asString());
        assertEquals("C400", upload(token(), new byte[2 * 1024 * 1024 + 1],
                "big.png", MediaType.IMAGE_PNG).get("code").asString());
        assertEquals("C400", upload(token(), "not an image".getBytes(),
                "fake.png", MediaType.IMAGE_PNG).get("code").asString());
        assertEquals("C400", upload(token(), "hello".getBytes(),
                "a.txt", MediaType.TEXT_PLAIN).get("code").asString());
    }
}
