package com.cdwater.ticketbox.order.api;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.order.api.dto.CreateShowOrderCommandDTO;
import com.cdwater.ticketbox.order.api.dto.ShowOrderDetailDTO;
import com.cdwater.ticketbox.order.api.dto.ShowOrderListItemDTO;

public interface ShowOrderApi {

    ShowOrderDetailDTO create(long userId, CreateShowOrderCommandDTO command) throws BizException;

    ShowOrderDetailDTO pay(long userId, long orderNo) throws BizException;

    ShowOrderDetailDTO cancel(long userId, long orderNo) throws BizException;

    ShowOrderDetailDTO detail(long userId, long orderNo) throws BizException;

    void expire(long orderNo) throws BizException;

    PageResult<ShowOrderListItemDTO> list(long userId, Integer status, long page, long size)
            throws BizException;
}
