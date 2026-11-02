package com.cdwater.ticketbox.user.adapter.out.cache;

import com.cdwater.ticketbox.user.application.port.CaptchaStore;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisCaptchaAdapter implements CaptchaStore {

    private static final String KEY_PREFIX = "captcha:user:";

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void save(String phone, String code, Duration ttl) {
        stringRedisTemplate.opsForValue().set(KEY_PREFIX + phone, code, ttl);
    }

    @Override
    public String consume(String phone) {
        return stringRedisTemplate.opsForValue().getAndDelete(KEY_PREFIX + phone);
    }
}
