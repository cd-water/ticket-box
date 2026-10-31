package com.cdwater.ticketbox.cinema.api.controller;

import com.cdwater.ticketbox.cinema.api.vo.SeatMapVO;
import com.cdwater.ticketbox.cinema.application.service.ScreeningQueryService;
import com.cdwater.ticketbox.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/screenings")
@RequiredArgsConstructor
public class ScreeningController {

    private final ScreeningQueryService screeningQueryService;

    @GetMapping("/{id}/seats")
    public Result<SeatMapVO> seats(@PathVariable Long id) {
        return Result.success(screeningQueryService.seatMap(id));
    }
}
