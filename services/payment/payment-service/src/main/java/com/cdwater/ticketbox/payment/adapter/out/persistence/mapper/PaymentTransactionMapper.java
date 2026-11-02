package com.cdwater.ticketbox.payment.adapter.out.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdwater.ticketbox.payment.adapter.out.persistence.po.PaymentTransactionPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentTransactionMapper extends BaseMapper<PaymentTransactionPO> {
}
