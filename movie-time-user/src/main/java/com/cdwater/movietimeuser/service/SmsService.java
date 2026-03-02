package com.cdwater.movietimeuser.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsService {

    public void send(String phone, String code) {
        //TODO 集成第三方短信服务
        log.info("发送验证码：phone={}, code={}", phone, code);
    }
}
