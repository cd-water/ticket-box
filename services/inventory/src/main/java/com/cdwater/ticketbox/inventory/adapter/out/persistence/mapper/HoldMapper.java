package com.cdwater.ticketbox.inventory.adapter.out.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdwater.ticketbox.inventory.adapter.out.persistence.po.HoldPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface HoldMapper extends BaseMapper<HoldPO> {

    @Update("UPDATE t_hold SET status = 1 WHERE order_no = #{orderNo} AND status = 0")
    int markConfirmed(@Param("orderNo") Long orderNo);

    @Update("UPDATE t_hold SET status = 2 WHERE order_no = #{orderNo} AND status <> 2")
    int markReleased(@Param("orderNo") Long orderNo);
}
