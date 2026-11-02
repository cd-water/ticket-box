package com.cdwater.ticketbox.show.adapter.out.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_show_order")
public class ShowOrderPO {
    @TableId(value = "order_no", type = IdType.INPUT)
    private Long orderNo;
    private Long userId;
    private Long showId;
    private String showTitle;
    private String venueName;
    private String venueAddress;
    private LocalDateTime startTime;
    private Long ticketTypeId;
    private String ticketTypeName;
    private String zone;
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
