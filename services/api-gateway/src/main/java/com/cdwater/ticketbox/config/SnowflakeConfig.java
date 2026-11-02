package com.cdwater.ticketbox.config;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SnowflakeConfig {

    @Value("${app.snowflake.worker-id:0}")
    private long workerId;

    @Value("${app.snowflake.datacenter-id:0}")
    private long datacenterId;

    @PostConstruct
    public void init() {
        if (workerId < 0 || workerId > 31 || datacenterId < 0 || datacenterId > 31) {
            throw new IllegalStateException("snowflake workerId/datacenterId must be 0..31");
        }
        IdWorker.initSequence(workerId, datacenterId);
    }
}
