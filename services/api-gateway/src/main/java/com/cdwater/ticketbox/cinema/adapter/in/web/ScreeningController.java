package com.cdwater.ticketbox.cinema.adapter.in.web;

import com.cdwater.ticketbox.cinema.application.query.view.SeatMapView;
import com.cdwater.ticketbox.cinema.application.query.ScreeningQueryService;
import com.cdwater.ticketbox.shared.web.Result;
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
    public Result<SeatMapView> seats(@PathVariable Long id) {
        return Result.success(screeningQueryService.seatMap(id));
    }
}
