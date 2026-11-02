package com.cdwater.ticketbox.order.adapter.in.rpc;

import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.common.ticket.ProductType;
import com.cdwater.ticketbox.order.api.BookingApi;
import com.cdwater.ticketbox.order.api.dto.BookingDetailDTO;
import com.cdwater.ticketbox.order.api.dto.BookingListItemDTO;
import com.cdwater.ticketbox.order.api.dto.CreateBookingCommandDTO;
import com.cdwater.ticketbox.order.application.command.BookingCommandService;
import com.cdwater.ticketbox.order.application.query.BookingQueryService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
@RequiredArgsConstructor
public class BookingApiImpl implements BookingApi {

    private final BookingCommandService commandService;
    private final BookingQueryService queryService;

    @Override
    public BookingDetailDTO create(long userId, CreateBookingCommandDTO command) {
        return commandService.createOrder(userId, command);
    }

    @Override
    public BookingDetailDTO pay(long userId, long orderNo) {
        return commandService.payOrder(userId, orderNo);
    }

    @Override
    public BookingDetailDTO cancel(long userId, long orderNo) {
        return commandService.cancelOrder(userId, orderNo);
    }

    @Override
    public BookingDetailDTO getDetail(long userId, long orderNo) {
        return queryService.getDetail(userId, orderNo);
    }

    @Override
    public void expire(long orderNo) {
        commandService.expireOrder(orderNo);
    }

    @Override
    public PageResult<BookingListItemDTO> list(long userId, ProductType productType, Integer status,
                                               long page, long size) {
        return queryService.list(userId, productType, status == null ? 0 : status, page, size);
    }
}
