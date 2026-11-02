package com.cdwater.ticketbox.cinema.adapter.in.web;

import com.cdwater.ticketbox.cinema.application.command.CreateMovieOrderCommand;
import com.cdwater.ticketbox.cinema.application.command.MovieOrderCommandService;
import com.cdwater.ticketbox.cinema.application.query.MovieOrderListQuery;
import com.cdwater.ticketbox.cinema.application.query.MovieOrderQueryService;
import com.cdwater.ticketbox.cinema.application.query.view.MovieOrderDetailView;
import com.cdwater.ticketbox.cinema.application.query.view.MovieOrderListItemView;
import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.common.security.CurrentUser;
import com.cdwater.ticketbox.common.result.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    private final MovieOrderCommandService orderCommandService;
    private final MovieOrderQueryService orderQueryService;
    private final CurrentUser currentUser;

    @PostMapping
    public Result<MovieOrderDetailView> create(@Valid @RequestBody CreateMovieOrderCommand command) {
        return Result.success(orderCommandService.createOrder(currentUser.id(), command));
    }

    @GetMapping("/{orderNo}")
    public Result<MovieOrderDetailView> detail(@PathVariable Long orderNo) {
        return Result.success(orderQueryService.getOrder(currentUser.id(), orderNo));
    }

    @GetMapping
    public Result<PageResult<MovieOrderListItemView>> list(@Valid MovieOrderListQuery query) {
        return Result.success(orderQueryService.list(
                currentUser.id(),
                query.getStatus(), query.getPage(), query.getSize()));
    }

    @PostMapping("/{orderNo}/pay")
    public Result<MovieOrderDetailView> pay(@PathVariable Long orderNo) {
        return Result.success(orderCommandService.pay(currentUser.id(), orderNo));
    }

    @PostMapping("/{orderNo}/cancel")
    public Result<MovieOrderDetailView> cancel(@PathVariable Long orderNo) {
        return Result.success(orderCommandService.cancel(currentUser.id(), orderNo));
    }
}