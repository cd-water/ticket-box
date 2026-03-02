package com.cdwater.movietimeorder.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PayService {

    public boolean pay() {
        //TODO 集成第三方支付
        log.info("调用支付接口");
        return true;
    }
}
