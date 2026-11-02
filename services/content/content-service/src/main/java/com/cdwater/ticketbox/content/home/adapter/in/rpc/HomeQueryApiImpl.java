package com.cdwater.ticketbox.content.home.adapter.in.rpc;

import com.cdwater.ticketbox.content.api.HomeQueryApi;
import com.cdwater.ticketbox.content.api.dto.HomeDTO;
import com.cdwater.ticketbox.content.home.application.query.HomeQueryService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
@RequiredArgsConstructor
public class HomeQueryApiImpl implements HomeQueryApi {

    private final HomeQueryService homeQueryService;

    @Override
    public HomeDTO home() {
        return homeQueryService.home();
    }
}
