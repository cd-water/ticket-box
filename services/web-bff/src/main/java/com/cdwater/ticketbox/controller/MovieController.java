package com.cdwater.ticketbox.controller;

import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.common.result.Result;
import com.cdwater.ticketbox.content.api.MovieQueryApi;
import com.cdwater.ticketbox.content.api.dto.MovieDetailDTO;
import com.cdwater.ticketbox.content.api.dto.MovieListItemDTO;
import com.cdwater.ticketbox.content.api.dto.MovieListQueryDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    @DubboReference(check = false, timeout = 3000)
    private MovieQueryApi movieQueryApi;

    @GetMapping
    public Result<PageResult<MovieListItemDTO>> list(@Valid MovieListQueryDTO query) {
        return Result.success(movieQueryApi.list(query));
    }

    @GetMapping("/{id}")
    public Result<MovieDetailDTO> getDetail(@PathVariable Long id) {
        return Result.success(movieQueryApi.getDetail(id));
    }
}
