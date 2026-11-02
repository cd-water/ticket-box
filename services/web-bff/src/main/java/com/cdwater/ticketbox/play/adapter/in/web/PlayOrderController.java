package com.cdwater.ticketbox.play.adapter.in.web;

import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.common.result.Result;
import com.cdwater.ticketbox.common.security.CurrentUser;
import com.cdwater.ticketbox.order.api.PlayOrderApi;
import com.cdwater.ticketbox.order.api.dto.CreatePlayOrderCommandDTO;
import com.cdwater.ticketbox.order.api.dto.PlayOrderDetailDTO;
import com.cdwater.ticketbox.order.api.dto.PlayOrderListItemDTO;
import com.cdwater.ticketbox.order.api.dto.PlayOrderListQueryDTO;
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
    private PlayOrderApi playOrderApi;

    private final CurrentUser currentUser;

    @PostMapping
    public Result<PlayOrderDetailDTO> create(@Valid @RequestBody CreatePlayOrderCommandDTO command) {
        return Result.success(playOrderApi.create(currentUser.id(), command));
    }

    @GetMapping("/{orderNo}")
    public Result<PlayOrderDetailDTO> detail(@PathVariable Long orderNo) {
        return Result.success(playOrderApi.detail(currentUser.id(), orderNo));
    }

    @GetMapping
    public Result<PageResult<PlayOrderListItemDTO>> list(@Valid PlayOrderListQueryDTO query) {
        return Result.success(playOrderApi.list(
                currentUser.id(), query.getStatus(), query.getPage(), query.getSize()));
    }

    @PostMapping("/{orderNo}/pay")
    public Result<PlayOrderDetailDTO> pay(@PathVariable Long orderNo) {
        return Result.success(playOrderApi.pay(currentUser.id(), orderNo));
    }

    @PostMapping("/{orderNo}/cancel")
    public Result<PlayOrderDetailDTO> cancel(@PathVariable Long orderNo) {
        return Result.success(playOrderApi.cancel(currentUser.id(), orderNo));
    }
}
