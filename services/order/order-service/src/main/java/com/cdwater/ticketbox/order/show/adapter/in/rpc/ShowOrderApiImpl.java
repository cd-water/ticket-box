package com.cdwater.ticketbox.order.show.adapter.in.rpc;

import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.order.api.ShowOrderApi;
import com.cdwater.ticketbox.order.api.dto.CreateShowOrderCommandDTO;
import com.cdwater.ticketbox.order.api.dto.ShowOrderDetailDTO;
import com.cdwater.ticketbox.order.api.dto.ShowOrderListItemDTO;
import com.cdwater.ticketbox.order.show.application.command.ShowOrderCommandService;
import com.cdwater.ticketbox.order.show.application.query.ShowOrderQueryService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
@RequiredArgsConstructor
public class ShowOrderApiImpl implements ShowOrderApi {

    private final ShowOrderCommandService commandService;
    private final ShowOrderQueryService queryService;

    @Override
    public ShowOrderDetailDTO create(long userId, CreateShowOrderCommandDTO command) {
        return commandService.createOrder(userId, command);
    }

    @Override
    public ShowOrderDetailDTO pay(long userId, long orderNo) {
        return commandService.pay(userId, orderNo);
    }

    @Override
    public ShowOrderDetailDTO cancel(long userId, long orderNo) {
        return commandService.cancel(userId, orderNo);
    }

    @Override
    public ShowOrderDetailDTO detail(long userId, long orderNo) {
        return queryService.getOrder(userId, orderNo);
    }

    @Override
    public PageResult<ShowOrderListItemDTO> list(long userId, Integer status, long page, long size) {
        return queryService.list(userId, status, page, size);
    }
    @Override
    public void expire(long orderNo) {
        commandService.expire(orderNo);
    }
}
