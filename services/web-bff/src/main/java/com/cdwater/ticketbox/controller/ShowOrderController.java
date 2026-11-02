package com.cdwater.ticketbox.controller;

import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.common.result.Result;
import com.cdwater.ticketbox.common.security.CurrentUser;
import com.cdwater.ticketbox.common.ticket.ProductType;
import com.cdwater.ticketbox.order.OrderListQuery;
import com.cdwater.ticketbox.order.OrderViewConverter;
import com.cdwater.ticketbox.order.CreateShowOrderRequest;
import com.cdwater.ticketbox.order.ShowOrderView;
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
@RequestMapping("/api/show-orders")
@RequiredArgsConstructor
public class ShowOrderController {

    @DubboReference(check = false, timeout = 5000)
    private BookingApi bookingApi;

    private final CurrentUser currentUser;
    private final OrderViewConverter viewConverter;

    @PostMapping
    public Result<ShowOrderView> create(@Valid @RequestBody CreateShowOrderRequest request) {
        CreateBookingCommandDTO command = new CreateBookingCommandDTO();
        command.setProductType(ProductType.SHOW);
        command.setProductId(request.getShowId());
        command.setTicketTypeId(request.getTicketTypeId());
        command.setQuantity(request.getQuantity());
        return Result.success(viewConverter.toShow(bookingApi.create(currentUser.id(), command)));
    }

    @GetMapping("/{orderNo}")
    public Result<ShowOrderView> getDetail(@PathVariable Long orderNo) {
        return Result.success(viewConverter.toShow(bookingApi.getDetail(currentUser.id(), orderNo)));
    }

    @GetMapping
    public Result<PageResult<ShowOrderView>> list(@Valid OrderListQuery query) {
        PageResult<BookingListItemDTO> page = bookingApi.list(currentUser.id(), ProductType.SHOW,
                query.getStatus(), query.getPage(), query.getSize());
        return Result.success(PageResult.of(page.getTotal(),
                page.getRecords().stream().map(viewConverter::toShow).toList(),
                page.getPage(), page.getSize()));
    }

    @PostMapping("/{orderNo}/pay")
    public Result<ShowOrderView> pay(@PathVariable Long orderNo) {
        return Result.success(viewConverter.toShow(bookingApi.pay(currentUser.id(), orderNo)));
    }

    @PostMapping("/{orderNo}/cancel")
    public Result<ShowOrderView> cancel(@PathVariable Long orderNo) {
        return Result.success(viewConverter.toShow(bookingApi.cancel(currentUser.id(), orderNo)));
    }
}
