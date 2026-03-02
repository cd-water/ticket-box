package com.cdwater.movietimeorder.mapper;

import com.cdwater.movietimeorder.model.dto.OrderPageDTO;
import com.cdwater.movietimeorder.model.entity.Order;
import com.cdwater.movietimeorder.model.vo.OrderListVO;
import com.cdwater.movietimeorder.model.vo.OrderPageVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {

    List<OrderPageVO> selectPage(OrderPageDTO orderPageDTO);

    Long count();

    List<OrderListVO> selectByUserId(Long userId);

    void insert(Order order);

    int updateStatus(Long orderNo, Integer status);

    Order selectByOrderNo(Long orderNo);

    List<Order> selectRecentlyOneDay();
}
