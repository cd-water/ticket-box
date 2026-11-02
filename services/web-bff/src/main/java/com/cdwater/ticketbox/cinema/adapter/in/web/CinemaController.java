package com.cdwater.ticketbox.cinema.adapter.in.web;

import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.common.result.Result;
import com.cdwater.ticketbox.content.api.CinemaQueryApi;
import com.cdwater.ticketbox.content.api.ScreeningQueryApi;
import com.cdwater.ticketbox.content.api.dto.CinemaDetailDTO;
import com.cdwater.ticketbox.content.api.dto.CinemaListItemDTO;
import com.cdwater.ticketbox.content.api.dto.CinemaListQueryDTO;
import com.cdwater.ticketbox.content.api.dto.ScreeningDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cinemas")
@RequiredArgsConstructor
public class CinemaController {

    @DubboReference(check = false, timeout = 3000)
    private CinemaQueryApi cinemaQueryApi;

    @DubboReference(check = false, timeout = 3000)
    private ScreeningQueryApi screeningQueryApi;

    @GetMapping
    public Result<PageResult<CinemaListItemDTO>> list(@Valid CinemaListQueryDTO query) {
        return Result.success(cinemaQueryApi.list(query));
    }

    @GetMapping("/{id}")
    public Result<CinemaDetailDTO> detail(@PathVariable Long id) {
        return Result.success(cinemaQueryApi.detail(id));
    }

    @GetMapping("/{cinemaId}/movies/{movieId}/screenings")
    public Result<List<ScreeningDTO>> screenings(@PathVariable Long cinemaId,
                                                @PathVariable Long movieId) {
        return Result.success(screeningQueryApi.list(cinemaId, movieId));
    }
}
