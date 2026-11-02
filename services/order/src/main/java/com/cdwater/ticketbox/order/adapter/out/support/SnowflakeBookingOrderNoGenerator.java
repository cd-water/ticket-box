package com.cdwater.ticketbox.order.adapter.out.support;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.cdwater.ticketbox.order.application.port.BookingOrderNoGenerator;
import org.springframework.stereotype.Component;

@Component
public class SnowflakeBookingOrderNoGenerator implements BookingOrderNoGenerator {

    @Override
    public long nextOrderNo() {
        return IdWorker.getId();
    }
}
