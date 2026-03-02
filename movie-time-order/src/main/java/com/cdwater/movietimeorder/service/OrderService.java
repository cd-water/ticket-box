package com.cdwater.movietimeorder.service;

import com.cdwater.movietimeorder.model.dto.OrderPageDTO;
import com.cdwater.movietimeorder.model.dto.OrderPlaceDTO;
import com.cdwater.movietimeorder.model.vo.OrderListVO;
import com.cdwater.movietimeorder.model.vo.OrderPageVO;
import com.cdwater.movietimeorder.model.vo.OrderPlaceVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface OrderService {

    PageInfo<OrderPageVO> page(OrderPageDTO orderPageDTO);

    Long count();

    List<OrderListVO> list();

    OrderPlaceVO place(OrderPlaceDTO orderPlaceDTO);

    void pay(Long orderNo);

    void cancel(Long orderNo);

    boolean cancelMQ(Long orderNo);
}
