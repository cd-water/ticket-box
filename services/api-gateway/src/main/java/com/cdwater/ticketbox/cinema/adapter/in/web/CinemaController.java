package com.cdwater.ticketbox.cinema.adapter.in.web;

import com.cdwater.ticketbox.cinema.application.query.CinemaListQuery;
import com.cdwater.ticketbox.cinema.application.query.view.CinemaDetailView;
import com.cdwater.ticketbox.cinema.application.query.view.CinemaListItemView;
import com.cdwater.ticketbox.cinema.application.query.view.ScreeningView;
import com.cdwater.ticketbox.cinema.application.query.CinemaQueryService;
import com.cdwater.ticketbox.cinema.application.query.ScreeningQueryService;
import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.common.result.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cinemas")
@RequiredArgsConstructor
public class CinemaController {

    private final CinemaQueryService cinemaQueryService;
    private final ScreeningQueryService screeningQueryService;

    @GetMapping
    public Result<PageResult<CinemaListItemView>> list(@Valid CinemaListQuery query) {
        return Result.success(cinemaQueryService.list(query));
    }

    @GetMapping("/{id}")
    public Result<CinemaDetailView> detail(@PathVariable Long id) {
        return Result.success(cinemaQueryService.detail(id));
    }

    @GetMapping("/{cinemaId}/movies/{movieId}/screenings")
    public Result<List<ScreeningView>> screenings(@PathVariable Long cinemaId,
                                                @PathVariable Long movieId) {
        return Result.success(screeningQueryService.list(cinemaId, movieId));
    }
}
