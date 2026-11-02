package com.cdwater.ticketbox.play.adapter.out.support;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.cdwater.ticketbox.play.application.port.PlayOrderNoGenerator;
import org.springframework.stereotype.Component;

@Component
public class SnowflakePlayOrderNoGenerator implements PlayOrderNoGenerator {

    @Override
    public long nextOrderNo() {
        return IdWorker.getId();
    }
}
