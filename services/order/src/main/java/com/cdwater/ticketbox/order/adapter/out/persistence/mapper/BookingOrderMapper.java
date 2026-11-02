package com.cdwater.ticketbox.order.adapter.out.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdwater.ticketbox.order.adapter.out.persistence.po.BookingOrderPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

@Mapper
public interface BookingOrderMapper extends BaseMapper<BookingOrderPO> {

    @Update("""
            UPDATE t_booking_order SET status = 1, pay_time = #{payTime}
            WHERE order_no = #{orderNo} AND status = 0 AND expire_time > #{payTime}
            """)
    int markPaid(@Param("orderNo") Long orderNo, @Param("payTime") LocalDateTime payTime);

    @Update("UPDATE t_booking_order SET status = 2 WHERE order_no = #{orderNo} AND status = 0")
    int markCancelled(@Param("orderNo") Long orderNo);

    @Update("UPDATE t_booking_order SET status = 2 WHERE order_no = #{orderNo} AND status = 1")
    int markCancelledFromPaid(@Param("orderNo") Long orderNo);
}
