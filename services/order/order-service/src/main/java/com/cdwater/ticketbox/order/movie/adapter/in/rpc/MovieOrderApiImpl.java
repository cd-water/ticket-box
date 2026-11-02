package com.cdwater.ticketbox.order.movie.adapter.in.rpc;

import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.order.api.MovieOrderApi;
import com.cdwater.ticketbox.order.api.dto.CreateMovieOrderCommandDTO;
import com.cdwater.ticketbox.order.api.dto.MovieOrderDetailDTO;
import com.cdwater.ticketbox.order.api.dto.MovieOrderListItemDTO;
import com.cdwater.ticketbox.order.movie.application.command.MovieOrderCommandService;
import com.cdwater.ticketbox.order.movie.application.query.MovieOrderQueryService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
@RequiredArgsConstructor
public class MovieOrderApiImpl implements MovieOrderApi {

    private final MovieOrderCommandService commandService;
    private final MovieOrderQueryService queryService;

    @Override
    public MovieOrderDetailDTO create(long userId, CreateMovieOrderCommandDTO command) {
        return commandService.createOrder(userId, command);
    }

    @Override
    public MovieOrderDetailDTO pay(long userId, long orderNo) {
        return commandService.pay(userId, orderNo);
    }

    @Override
    public MovieOrderDetailDTO cancel(long userId, long orderNo) {
        return commandService.cancel(userId, orderNo);
    }

    @Override
    public MovieOrderDetailDTO detail(long userId, long orderNo) {
        return queryService.getOrder(userId, orderNo);
    }

    @Override
    public PageResult<MovieOrderListItemDTO> list(long userId, Integer status, long page, long size) {
        return queryService.list(userId, status, page, size);
    }
    @Override
    public void expire(long orderNo) {
        commandService.expireOrder(orderNo);
    }
}
