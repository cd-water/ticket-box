package com.cdwater.ticketbox.cinema.adapter.out.support;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.cdwater.ticketbox.cinema.application.port.MovieOrderNoGenerator;
import org.springframework.stereotype.Component;

@Component
public class SnowflakeMovieOrderNoGenerator implements MovieOrderNoGenerator {

    @Override
    public long nextOrderNo() {
        return IdWorker.getId();
    }
}
