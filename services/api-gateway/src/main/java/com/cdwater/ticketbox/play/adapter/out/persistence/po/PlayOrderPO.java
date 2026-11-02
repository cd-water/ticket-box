package com.cdwater.ticketbox.play.adapter.out.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_play_order")
public class PlayOrderPO {
    @TableId(value = "order_no", type = IdType.INPUT)
    private Long orderNo;
    private Long userId;
    private Long itemId;
    private String itemTitle;
    private String itemAddress;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal totalAmount;
    /** 0-待支付 1-已支付 2-已取消（含超时） */
    private Integer status;
    private LocalDateTime expireTime;
    private LocalDateTime payTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
