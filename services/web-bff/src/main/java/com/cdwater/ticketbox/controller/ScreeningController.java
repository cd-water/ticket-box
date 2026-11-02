package com.cdwater.ticketbox.controller;

import com.cdwater.ticketbox.common.result.Result;
import com.cdwater.ticketbox.content.api.ScreeningQueryApi;
import com.cdwater.ticketbox.content.api.dto.SeatMapDTO;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/screenings")
@RequiredArgsConstructor
public class ScreeningController {

    @DubboReference(check = false, timeout = 3000)
    private ScreeningQueryApi screeningQueryApi;

    @GetMapping("/{id}/seats")
    public Result<SeatMapDTO> getSeatMap(@PathVariable Long id) {
        return Result.success(screeningQueryApi.getSeatMap(id));
    }
}
