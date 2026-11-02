package com.cdwater.ticketbox.order.play.adapter.in.rpc;

import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.order.api.PlayOrderApi;
import com.cdwater.ticketbox.order.api.dto.CreatePlayOrderCommandDTO;
import com.cdwater.ticketbox.order.api.dto.PlayOrderDetailDTO;
import com.cdwater.ticketbox.order.api.dto.PlayOrderListItemDTO;
import com.cdwater.ticketbox.order.play.application.command.PlayOrderCommandService;
import com.cdwater.ticketbox.order.play.application.command.PlayOrderExpiryService;
import com.cdwater.ticketbox.order.play.application.query.PlayOrderQueryService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
@RequiredArgsConstructor
public class PlayOrderApiImpl implements PlayOrderApi {

    private final PlayOrderCommandService commandService;
    private final PlayOrderQueryService queryService;
    private final PlayOrderExpiryService expiryService;

    @Override
    public PlayOrderDetailDTO create(long userId, CreatePlayOrderCommandDTO command) {
        return commandService.createOrder(userId, command);
    }

    @Override
    public PlayOrderDetailDTO pay(long userId, long orderNo) {
        return commandService.pay(userId, orderNo);
    }

    @Override
    public PlayOrderDetailDTO cancel(long userId, long orderNo) {
        return commandService.cancel(userId, orderNo);
    }

    @Override
    public PlayOrderDetailDTO detail(long userId, long orderNo) {
        return queryService.getOrder(userId, orderNo);
    }

    @Override
    public PageResult<PlayOrderListItemDTO> list(long userId, Integer status, long page, long size) {
        return queryService.list(userId, status, page, size);
    }
    @Override
    public void expire(long orderNo) {
        expiryService.expire(orderNo);
    }
}
