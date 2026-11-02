package com.cdwater.ticketbox.order.api;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.order.api.dto.CreatePlayOrderCommandDTO;
import com.cdwater.ticketbox.order.api.dto.PlayOrderDetailDTO;
import com.cdwater.ticketbox.order.api.dto.PlayOrderListItemDTO;

public interface PlayOrderApi {

    PlayOrderDetailDTO create(long userId, CreatePlayOrderCommandDTO command) throws BizException;

    PlayOrderDetailDTO pay(long userId, long orderNo) throws BizException;

    PlayOrderDetailDTO cancel(long userId, long orderNo) throws BizException;

    PlayOrderDetailDTO detail(long userId, long orderNo) throws BizException;

    void expire(long orderNo) throws BizException;

    PageResult<PlayOrderListItemDTO> list(long userId, Integer status, long page, long size)
            throws BizException;
}
