package com.cdwater.ticketbox.movie.api.controller;

import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.common.result.Result;
import com.cdwater.ticketbox.movie.api.dto.MovieListQuery;
import com.cdwater.ticketbox.movie.application.service.MovieService;
import com.cdwater.ticketbox.movie.api.vo.MovieDetailVO;
import com.cdwater.ticketbox.movie.api.vo.MovieListItemVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping
    public Result<PageResult<MovieListItemVO>> list(@Valid MovieListQuery query) {
        return Result.success(movieService.list(query));
    }

    @GetMapping("/{id}")
    public Result<MovieDetailVO> detail(@PathVariable Long id) {
        return Result.success(movieService.detail(id));
    }
}
