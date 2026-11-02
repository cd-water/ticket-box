package com.cdwater.ticketbox.home.adapter.in.web;

import com.cdwater.ticketbox.common.result.Result;
import com.cdwater.ticketbox.content.api.HomeQueryApi;
import com.cdwater.ticketbox.content.api.dto.HomeDTO;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeController {

    @DubboReference(check = false, timeout = 3000)
    private HomeQueryApi homeQueryApi;

    @GetMapping
    public Result<HomeDTO> home() {
        return Result.success(homeQueryApi.home());
    }
}
