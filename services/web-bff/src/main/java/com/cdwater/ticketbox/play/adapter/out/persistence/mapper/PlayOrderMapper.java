package com.cdwater.ticketbox.play.adapter.out.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdwater.ticketbox.play.adapter.out.persistence.po.PlayOrderPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

@Mapper
public interface PlayOrderMapper extends BaseMapper<PlayOrderPO> {

    @Update("""
            UPDATE t_play_order SET status = 1, pay_time = #{payTime}
            WHERE order_no = #{orderNo} AND status = 0 AND expire_time > #{payTime}
            """)
    int markPaid(@Param("orderNo") Long orderNo, @Param("payTime") LocalDateTime payTime);

    @Update("""
            UPDATE t_play_order SET status = 0, pay_time = NULL
            WHERE order_no = #{orderNo} AND status = 1
            """)
    int markPending(@Param("orderNo") Long orderNo);

    @Update("UPDATE t_play_order SET status = 2 WHERE order_no = #{orderNo} AND status = 0")
    int markCancelled(@Param("orderNo") Long orderNo);
}
