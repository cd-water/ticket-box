package com.cdwater.ticketbox.booking.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdwater.ticketbox.booking.domain.model.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    @Update("UPDATE t_order SET status = 3 WHERE id = #{id} AND status = 0")
    int markTimeout(@Param("id") Long id);
}
