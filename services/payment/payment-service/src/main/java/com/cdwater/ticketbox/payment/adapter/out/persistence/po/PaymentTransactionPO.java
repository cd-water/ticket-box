package com.cdwater.ticketbox.payment.adapter.out.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_payment_transaction")
public class PaymentTransactionPO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String paymentNo;
    private String type;
    private BigDecimal amount;
    private String channelTxnNo;
    private LocalDateTime createTime;
}
