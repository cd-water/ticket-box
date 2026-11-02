package com.cdwater.ticketbox.payment.adapter.out.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_payment_order")
public class PaymentOrderPO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String paymentNo;
    private Long orderNo;
    private BigDecimal amount;
    private String channel;
    private String status;
    private String idempotencyKey;
    private LocalDateTime createTime;
    private LocalDateTime payTime;
}
