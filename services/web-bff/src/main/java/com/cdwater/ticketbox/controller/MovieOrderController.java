package com.cdwater.ticketbox.controller;

import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.common.result.Result;
import com.cdwater.ticketbox.common.security.CurrentUser;
import com.cdwater.ticketbox.common.ticket.ProductType;
import com.cdwater.ticketbox.order.CreateMovieOrderRequest;
import com.cdwater.ticketbox.order.MovieOrderView;
import com.cdwater.ticketbox.order.OrderListQuery;
import com.cdwater.ticketbox.order.OrderViewConverter;
import com.cdwater.ticketbox.order.api.BookingApi;
import com.cdwater.ticketbox.order.api.dto.BookingListItemDTO;
import com.cdwater.ticketbox.order.api.dto.CreateBookingCommandDTO;
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
    private BookingApi bookingApi;

    private final CurrentUser currentUser;
    private final OrderViewConverter viewConverter;

    @PostMapping
    public Result<MovieOrderView> create(@Valid @RequestBody CreateMovieOrderRequest request) {
        CreateBookingCommandDTO command = new CreateBookingCommandDTO();
        command.setProductType(ProductType.MOVIE);
        command.setProductId(request.getScreeningId());
        command.setSeatIds(request.getSeatIds());
        command.setQuantity(request.getSeatIds().size());
        return Result.success(viewConverter.toMovie(bookingApi.create(currentUser.id(), command)));
    }

    @GetMapping("/{orderNo}")
    public Result<MovieOrderView> getDetail(@PathVariable Long orderNo) {
        return Result.success(viewConverter.toMovie(bookingApi.getDetail(currentUser.id(), orderNo)));
    }

    @GetMapping
    public Result<PageResult<MovieOrderView>> list(@Valid OrderListQuery query) {
        PageResult<BookingListItemDTO> page = bookingApi.list(currentUser.id(), ProductType.MOVIE,
                query.getStatus(), query.getPage(), query.getSize());
        return Result.success(PageResult.of(page.getTotal(),
                page.getRecords().stream().map(viewConverter::toMovie).toList(),
                page.getPage(), page.getSize()));
    }

    @PostMapping("/{orderNo}/pay")
    public Result<MovieOrderView> pay(@PathVariable Long orderNo) {
        return Result.success(viewConverter.toMovie(bookingApi.pay(currentUser.id(), orderNo)));
    }

    @PostMapping("/{orderNo}/cancel")
    public Result<MovieOrderView> cancel(@PathVariable Long orderNo) {
        return Result.success(viewConverter.toMovie(bookingApi.cancel(currentUser.id(), orderNo)));
    }
}
