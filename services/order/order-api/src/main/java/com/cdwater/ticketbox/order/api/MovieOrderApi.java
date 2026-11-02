package com.cdwater.ticketbox.order.api;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.order.api.dto.CreateMovieOrderCommandDTO;
import com.cdwater.ticketbox.order.api.dto.MovieOrderDetailDTO;
import com.cdwater.ticketbox.order.api.dto.MovieOrderListItemDTO;

public interface MovieOrderApi {

    MovieOrderDetailDTO create(long userId, CreateMovieOrderCommandDTO command) throws BizException;

    MovieOrderDetailDTO pay(long userId, long orderNo) throws BizException;

    MovieOrderDetailDTO cancel(long userId, long orderNo) throws BizException;

    MovieOrderDetailDTO detail(long userId, long orderNo) throws BizException;

    void expire(long orderNo) throws BizException;

    PageResult<MovieOrderListItemDTO> list(long userId, Integer status, long page, long size)
            throws BizException;
}
