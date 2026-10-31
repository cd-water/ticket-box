package com.cdwater.ticketbox.cinema.api.controller;

import com.cdwater.ticketbox.cinema.api.dto.CinemaListQuery;
import com.cdwater.ticketbox.cinema.api.vo.CinemaDetailVO;
import com.cdwater.ticketbox.cinema.api.vo.CinemaListItemVO;
import com.cdwater.ticketbox.cinema.api.vo.ScreeningVO;
import com.cdwater.ticketbox.cinema.application.service.CinemaQueryService;
import com.cdwater.ticketbox.cinema.application.service.ScreeningQueryService;
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
    public Result<PageResult<CinemaListItemVO>> list(@Valid CinemaListQuery query) {
        return Result.success(cinemaQueryService.list(query));
    }

    @GetMapping("/{id}")
    public Result<CinemaDetailVO> detail(@PathVariable Long id) {
        return Result.success(cinemaQueryService.detail(id));
    }

    @GetMapping("/{cinemaId}/movies/{movieId}/screenings")
    public Result<List<ScreeningVO>> screenings(@PathVariable Long cinemaId,
                                                @PathVariable Long movieId) {
        return Result.success(screeningQueryService.list(cinemaId, movieId));
    }
}
