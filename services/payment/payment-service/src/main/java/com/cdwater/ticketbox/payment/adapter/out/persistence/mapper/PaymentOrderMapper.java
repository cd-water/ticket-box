package com.cdwater.ticketbox.payment.adapter.out.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdwater.ticketbox.payment.adapter.out.persistence.po.PaymentOrderPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface PaymentOrderMapper extends BaseMapper<PaymentOrderPO> {

    @Update("""
            UPDATE t_payment_order SET status = 'CLOSED'
            WHERE payment_no = #{paymentNo} AND status <> 'CLOSED'
            """)
    int markClosed(@Param("paymentNo") String paymentNo);
}
