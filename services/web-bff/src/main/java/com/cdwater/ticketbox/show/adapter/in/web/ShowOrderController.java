package com.cdwater.ticketbox.show.adapter.in.web;

import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.common.result.Result;
import com.cdwater.ticketbox.common.security.CurrentUser;
import com.cdwater.ticketbox.order.api.ShowOrderApi;
import com.cdwater.ticketbox.order.api.dto.CreateShowOrderCommandDTO;
import com.cdwater.ticketbox.order.api.dto.ShowOrderDetailDTO;
import com.cdwater.ticketbox.order.api.dto.ShowOrderListItemDTO;
import com.cdwater.ticketbox.order.api.dto.ShowOrderListQueryDTO;
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
    private ShowOrderApi showOrderApi;

    private final CurrentUser currentUser;

    @PostMapping
    public Result<ShowOrderDetailDTO> create(@Valid @RequestBody CreateShowOrderCommandDTO command) {
        return Result.success(showOrderApi.create(currentUser.id(), command));
    }

    @GetMapping("/{orderNo}")
    public Result<ShowOrderDetailDTO> detail(@PathVariable Long orderNo) {
        return Result.success(showOrderApi.detail(currentUser.id(), orderNo));
    }

    @GetMapping
    public Result<PageResult<ShowOrderListItemDTO>> list(@Valid ShowOrderListQueryDTO query) {
        return Result.success(showOrderApi.list(
                currentUser.id(), query.getStatus(), query.getPage(), query.getSize()));
    }

    @PostMapping("/{orderNo}/pay")
    public Result<ShowOrderDetailDTO> pay(@PathVariable Long orderNo) {
        return Result.success(showOrderApi.pay(currentUser.id(), orderNo));
    }

    @PostMapping("/{orderNo}/cancel")
    public Result<ShowOrderDetailDTO> cancel(@PathVariable Long orderNo) {
        return Result.success(showOrderApi.cancel(currentUser.id(), orderNo));
    }
}
