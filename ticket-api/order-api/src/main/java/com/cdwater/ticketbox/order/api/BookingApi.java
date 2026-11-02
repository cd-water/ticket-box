package com.cdwater.ticketbox.order.api;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.common.ticket.ProductType;
import com.cdwater.ticketbox.order.api.dto.BookingDetailDTO;
import com.cdwater.ticketbox.order.api.dto.BookingListItemDTO;
import com.cdwater.ticketbox.order.api.dto.CreateBookingCommandDTO;

/**
 * 统一订单契约：电影/演出/玩乐共用一个订单聚合与 Saga。
 */
public interface BookingApi {

    BookingDetailDTO create(long userId, CreateBookingCommandDTO command) throws BizException;

    BookingDetailDTO pay(long userId, long orderNo) throws BizException;

    BookingDetailDTO cancel(long userId, long orderNo) throws BizException;

    BookingDetailDTO getDetail(long userId, long orderNo) throws BizException;

    void expire(long orderNo) throws BizException;

    PageResult<BookingListItemDTO> list(long userId, ProductType productType, Integer status,
                                        long page, long size) throws BizException;
}
