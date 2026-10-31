package com.cdwater.ticketbox.booking.infrastructure.support;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.cdwater.ticketbox.booking.application.port.OrderNoGenerator;
import org.springframework.stereotype.Component;

@Component
public class SnowflakeOrderNoGenerator implements OrderNoGenerator {

    @Override
    public long nextOrderNo() {
        return IdWorker.getId();
    }
}
