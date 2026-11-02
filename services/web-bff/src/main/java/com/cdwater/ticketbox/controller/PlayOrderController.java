package com.cdwater.ticketbox.controller;

import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.common.result.Result;
import com.cdwater.ticketbox.common.security.CurrentUser;
import com.cdwater.ticketbox.common.ticket.ProductType;
import com.cdwater.ticketbox.order.OrderListQuery;
import com.cdwater.ticketbox.order.OrderViewConverter;
import com.cdwater.ticketbox.order.CreatePlayOrderRequest;
import com.cdwater.ticketbox.order.PlayOrderView;
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
@RequestMapping("/api/play-orders")
@RequiredArgsConstructor
public class PlayOrderController {

    @DubboReference(check = false, timeout = 5000)
    private BookingApi bookingApi;

    private final CurrentUser currentUser;
    private final OrderViewConverter viewConverter;

    @PostMapping
    public Result<PlayOrderView> create(@Valid @RequestBody CreatePlayOrderRequest request) {
        CreateBookingCommandDTO command = new CreateBookingCommandDTO();
        command.setProductType(ProductType.PLAY);
        command.setProductId(request.getItemId());
        command.setQuantity(request.getQuantity());
        return Result.success(viewConverter.toPlay(bookingApi.create(currentUser.id(), command)));
    }

    @GetMapping("/{orderNo}")
    public Result<PlayOrderView> getDetail(@PathVariable Long orderNo) {
        return Result.success(viewConverter.toPlay(bookingApi.getDetail(currentUser.id(), orderNo)));
    }

    @GetMapping
    public Result<PageResult<PlayOrderView>> list(@Valid OrderListQuery query) {
        PageResult<BookingListItemDTO> page = bookingApi.list(currentUser.id(), ProductType.PLAY,
                query.getStatus(), query.getPage(), query.getSize());
        return Result.success(PageResult.of(page.getTotal(),
                page.getRecords().stream().map(viewConverter::toPlay).toList(),
                page.getPage(), page.getSize()));
    }

    @PostMapping("/{orderNo}/pay")
    public Result<PlayOrderView> pay(@PathVariable Long orderNo) {
        return Result.success(viewConverter.toPlay(bookingApi.pay(currentUser.id(), orderNo)));
    }

    @PostMapping("/{orderNo}/cancel")
    public Result<PlayOrderView> cancel(@PathVariable Long orderNo) {
        return Result.success(viewConverter.toPlay(bookingApi.cancel(currentUser.id(), orderNo)));
    }
}
