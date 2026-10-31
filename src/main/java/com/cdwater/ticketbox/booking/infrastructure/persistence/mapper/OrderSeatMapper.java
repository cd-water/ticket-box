package com.cdwater.ticketbox.booking.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdwater.ticketbox.booking.domain.model.OrderSeat;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderSeatMapper extends BaseMapper<OrderSeat> {
}
