package com.cdwater.ticketbox.home.adapter.in.web;

import com.cdwater.ticketbox.home.application.query.HomeQueryService;
import com.cdwater.ticketbox.home.application.query.view.HomeView;
import com.cdwater.ticketbox.shared.web.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeController {

    private final HomeQueryService homeQueryService;

    @GetMapping
    public Result<HomeView> home() {
        return Result.success(homeQueryService.home());
    }
}
