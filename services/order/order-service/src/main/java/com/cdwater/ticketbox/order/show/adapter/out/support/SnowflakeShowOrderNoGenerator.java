package com.cdwater.ticketbox.order.show.adapter.out.support;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.cdwater.ticketbox.order.show.application.port.ShowOrderNoGenerator;
import org.springframework.stereotype.Component;

@Component
public class SnowflakeShowOrderNoGenerator implements ShowOrderNoGenerator {

    @Override
    public long nextOrderNo() {
        return IdWorker.getId();
    }
}
