package com.cdwater.ticketbox.booking.api.controller;

import com.cdwater.ticketbox.booking.api.dto.CreateOrderDTO;
import com.cdwater.ticketbox.booking.api.vo.OrderDetailVO;
import com.cdwater.ticketbox.booking.application.service.OrderApplicationService;
import com.cdwater.ticketbox.common.result.Result;
import com.cdwater.ticketbox.config.StpKit;
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

    private final OrderApplicationService orderApplicationService;

    @PostMapping
    public Result<OrderDetailVO> create(@Valid @RequestBody CreateOrderDTO dto) {
        return Result.success(orderApplicationService.createOrder(
                StpKit.USER.getLoginIdAsLong(), dto));
    }

    @GetMapping("/{id}")
    public Result<OrderDetailVO> detail(@PathVariable Long id) {
        return Result.success(orderApplicationService.getOrder(
                StpKit.USER.getLoginIdAsLong(), id));
    }
}
