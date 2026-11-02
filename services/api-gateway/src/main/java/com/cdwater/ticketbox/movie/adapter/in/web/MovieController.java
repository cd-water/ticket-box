package com.cdwater.ticketbox.movie.adapter.in.web;

import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.common.result.Result;
import com.cdwater.ticketbox.movie.application.query.MovieListQuery;
import com.cdwater.ticketbox.movie.application.query.MovieCatalogQueryService;
import com.cdwater.ticketbox.movie.application.query.view.MovieDetailView;
import com.cdwater.ticketbox.movie.application.query.view.MovieListItemView;
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

    private final MovieCatalogQueryService movieCatalogQueryService;

    @GetMapping
    public Result<PageResult<MovieListItemView>> list(@Valid MovieListQuery query) {
        return Result.success(movieCatalogQueryService.list(query));
    }

    @GetMapping("/{id}")
    public Result<MovieDetailView> detail(@PathVariable Long id) {
        return Result.success(movieCatalogQueryService.detail(id));
    }
}
