package com.cdwater.ticketbox.show.adapter.in.web;

import com.cdwater.ticketbox.shared.query.PageResult;
import com.cdwater.ticketbox.shared.security.CurrentUser;
import com.cdwater.ticketbox.shared.web.Result;
import com.cdwater.ticketbox.show.application.command.CreateShowOrderCommand;
import com.cdwater.ticketbox.show.application.command.ShowOrderCommandService;
import com.cdwater.ticketbox.show.application.query.ShowOrderListQuery;
import com.cdwater.ticketbox.show.application.query.ShowOrderQueryService;
import com.cdwater.ticketbox.show.application.query.view.ShowOrderDetailView;
import com.cdwater.ticketbox.show.application.query.view.ShowOrderListItemView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    private final ShowOrderCommandService orderCommandService;
    private final ShowOrderQueryService orderQueryService;
    private final CurrentUser currentUser;

    @PostMapping
    public Result<ShowOrderDetailView> create(@Valid @RequestBody CreateShowOrderCommand command) {
        return Result.success(orderCommandService.createOrder(currentUser.id(), command));
    }

    @GetMapping("/{orderNo}")
    public Result<ShowOrderDetailView> detail(@PathVariable Long orderNo) {
        return Result.success(orderQueryService.getOrder(currentUser.id(), orderNo));
    }

    @GetMapping
    public Result<PageResult<ShowOrderListItemView>> list(@Valid ShowOrderListQuery query) {
        return Result.success(orderQueryService.list(
                currentUser.id(),
                query.getStatus(), query.getPage(), query.getSize()));
    }

    @PostMapping("/{orderNo}/pay")
    public Result<ShowOrderDetailView> pay(@PathVariable Long orderNo) {
        return Result.success(orderCommandService.pay(currentUser.id(), orderNo));
    }

    @PostMapping("/{orderNo}/cancel")
    public Result<ShowOrderDetailView> cancel(@PathVariable Long orderNo) {
        return Result.success(orderCommandService.cancel(currentUser.id(), orderNo));
    }
}
