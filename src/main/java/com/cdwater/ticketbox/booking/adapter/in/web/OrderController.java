package com.cdwater.ticketbox.booking.adapter.in.web;

import com.cdwater.ticketbox.booking.application.command.OrderCommandService;
import com.cdwater.ticketbox.booking.application.command.CreateOrderCommand;
import com.cdwater.ticketbox.booking.application.query.OrderQueryService;
import com.cdwater.ticketbox.booking.application.query.view.OrderDetailView;
import com.cdwater.ticketbox.shared.web.Result;
import com.cdwater.ticketbox.config.SaTokenSetup;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderCommandService orderCommandService;
    private final OrderQueryService orderQueryService;

    @PostMapping
    public Result<OrderDetailView> create(@Valid @RequestBody CreateOrderCommand command) {
        return Result.success(orderCommandService.createOrder(
                SaTokenSetup.USER.getLoginIdAsLong(), command));
    }

    @GetMapping("/{orderNo}")
    public Result<OrderDetailView> detail(@PathVariable Long orderNo) {
        return Result.success(orderQueryService.getOrder(
                SaTokenSetup.USER.getLoginIdAsLong(), orderNo));
    }
}
