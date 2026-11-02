package com.cdwater.ticketbox.cinema.adapter.in.web;

import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.common.result.Result;
import com.cdwater.ticketbox.common.security.CurrentUser;
import com.cdwater.ticketbox.order.api.MovieOrderApi;
import com.cdwater.ticketbox.order.api.dto.CreateMovieOrderCommandDTO;
import com.cdwater.ticketbox.order.api.dto.MovieOrderDetailDTO;
import com.cdwater.ticketbox.order.api.dto.MovieOrderListItemDTO;
import com.cdwater.ticketbox.order.api.dto.MovieOrderListQueryDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movie-orders")
@RequiredArgsConstructor
public class MovieOrderController {

    @DubboReference(check = false, timeout = 5000)
    private MovieOrderApi movieOrderApi;

    private final CurrentUser currentUser;

    @PostMapping
    public Result<MovieOrderDetailDTO> create(@Valid @RequestBody CreateMovieOrderCommandDTO command) {
        return Result.success(movieOrderApi.create(currentUser.id(), command));
    }

    @GetMapping("/{orderNo}")
    public Result<MovieOrderDetailDTO> detail(@PathVariable Long orderNo) {
        return Result.success(movieOrderApi.detail(currentUser.id(), orderNo));
    }

    @GetMapping
    public Result<PageResult<MovieOrderListItemDTO>> list(@Valid MovieOrderListQueryDTO query) {
        return Result.success(movieOrderApi.list(
                currentUser.id(), query.getStatus(), query.getPage(), query.getSize()));
    }

    @PostMapping("/{orderNo}/pay")
    public Result<MovieOrderDetailDTO> pay(@PathVariable Long orderNo) {
        return Result.success(movieOrderApi.pay(currentUser.id(), orderNo));
    }

    @PostMapping("/{orderNo}/cancel")
    public Result<MovieOrderDetailDTO> cancel(@PathVariable Long orderNo) {
        return Result.success(movieOrderApi.cancel(currentUser.id(), orderNo));
    }
}
