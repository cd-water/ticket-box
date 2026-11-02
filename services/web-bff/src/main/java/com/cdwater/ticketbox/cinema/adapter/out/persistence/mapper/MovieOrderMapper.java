package com.cdwater.ticketbox.cinema.adapter.out.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdwater.ticketbox.cinema.adapter.out.persistence.po.MovieOrderPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MovieOrderMapper extends BaseMapper<MovieOrderPO> {

    @Update("UPDATE t_movie_order SET status = 2 WHERE order_no = #{orderNo} AND status = 0")
    int markCancelled(@Param("orderNo") Long orderNo);

    @Update("""
            UPDATE t_movie_order SET status = 1, pay_time = #{payTime}
            WHERE order_no = #{orderNo} AND status = 0 AND expire_time > #{payTime}
            """)
    int markPaid(@Param("orderNo") Long orderNo, @Param("payTime") java.time.LocalDateTime payTime);

    @Update("""
            UPDATE t_movie_order SET status = 0, pay_time = NULL
            WHERE order_no = #{orderNo} AND status = 1
            """)
    int markPending(@Param("orderNo") Long orderNo);
}
