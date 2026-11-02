package com.cdwater.ticketbox.play.adapter.in.web;

import com.cdwater.ticketbox.play.application.command.CreatePlayOrderCommand;
import com.cdwater.ticketbox.play.application.command.PlayOrderCommandService;
import com.cdwater.ticketbox.play.application.query.PlayOrderListQuery;
import com.cdwater.ticketbox.play.application.query.PlayOrderQueryService;
import com.cdwater.ticketbox.play.application.query.view.PlayOrderDetailView;
import com.cdwater.ticketbox.play.application.query.view.PlayOrderListItemView;
import com.cdwater.ticketbox.shared.query.PageResult;
import com.cdwater.ticketbox.shared.security.CurrentUser;
import com.cdwater.ticketbox.shared.web.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    private final PlayOrderCommandService orderCommandService;
    private final PlayOrderQueryService orderQueryService;
    private final CurrentUser currentUser;

    @PostMapping
    public Result<PlayOrderDetailView> create(@Valid @RequestBody CreatePlayOrderCommand command) {
        return Result.success(orderCommandService.createOrder(currentUser.id(), command));
    }

    @GetMapping("/{orderNo}")
    public Result<PlayOrderDetailView> detail(@PathVariable Long orderNo) {
        return Result.success(orderQueryService.getOrder(currentUser.id(), orderNo));
    }

    @GetMapping
    public Result<PageResult<PlayOrderListItemView>> list(@Valid PlayOrderListQuery query) {
        return Result.success(orderQueryService.list(
                currentUser.id(),
                query.getStatus(), query.getPage(), query.getSize()));
    }

    @PostMapping("/{orderNo}/pay")
    public Result<PlayOrderDetailView> pay(@PathVariable Long orderNo) {
        return Result.success(orderCommandService.pay(currentUser.id(), orderNo));
    }

    @PostMapping("/{orderNo}/cancel")
    public Result<PlayOrderDetailView> cancel(@PathVariable Long orderNo) {
        return Result.success(orderCommandService.cancel(currentUser.id(), orderNo));
    }
}
