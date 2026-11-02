package com.cdwater.ticketbox.inventory.adapter.out.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_hold")
public class HoldPO {

    @TableId(value = "order_no", type = IdType.INPUT)
    private Long orderNo;
    private String productType;
    private Long productId;
    private String zone;
    private Integer quantity;
    /** 0-已预留 1-已确认 2-已释放 */
    private Integer status;
    private LocalDateTime expireTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
