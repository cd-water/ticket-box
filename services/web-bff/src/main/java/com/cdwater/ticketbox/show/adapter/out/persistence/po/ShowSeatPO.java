package com.cdwater.ticketbox.show.adapter.out.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_show_seat")
public class ShowSeatPO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long showId;
    private String zone;
    private Integer seatNo;
    private Integer status;
    private Long lockOrderNo;
    private LocalDateTime lockExpireTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
