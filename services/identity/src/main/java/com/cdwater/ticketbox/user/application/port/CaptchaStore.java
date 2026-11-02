package com.cdwater.ticketbox.user.application.port;

import java.time.Duration;

public interface CaptchaStore {

    void save(String phone, String code, Duration ttl);

    String consume(String phone);
}
