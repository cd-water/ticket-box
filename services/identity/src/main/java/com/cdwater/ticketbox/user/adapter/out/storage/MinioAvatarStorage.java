package com.cdwater.ticketbox.user.adapter.out.storage;

import com.cdwater.ticketbox.config.MinioProperties;
import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.ResultCode;
import com.cdwater.ticketbox.user.application.port.AvatarStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class MinioAvatarStorage implements AvatarStorage {

    private final S3Client s3Client;
    private final MinioProperties properties;

    @Override
    public String upload(Long userId, byte[] content, String contentType, String extension) {
        String key = "avatars/" + userId + "/"
                + UUID.randomUUID().toString().replace("-", "") + "." + extension;
        try {
            s3Client.putObject(builder -> builder
                    .bucket(properties.getBucket())
                    .key(key)
                    .contentType(contentType), RequestBody.fromBytes(content));
            return baseUrl() + "/" + key;
        } catch (Exception e) {
            log.error("avatar upload failed, userId={}", userId, e);
            throw new BizException(ResultCode.INTERNAL_SERVER_ERROR, "头像上传失败");
        }
    }

    @Override
    public void deleteIfOwned(String url) {
        if (url == null || url.isBlank()) {
            return;
        }
        String prefix = baseUrl() + "/";
        if (!url.startsWith(prefix)) {
            return;
        }
        String key = url.substring(prefix.length());
        try {
            s3Client.deleteObject(builder -> builder.bucket(properties.getBucket()).key(key));
        } catch (Exception e) {
            log.warn("delete old avatar failed, url={}", url, e);
        }
    }

    private String baseUrl() {
        String base = properties.getPublicBaseUrl();
        if (base.endsWith("/")) {
            base = base.substring(0, base.length() - 1);
        }
        return base + "/" + properties.getBucket();
    }
}
