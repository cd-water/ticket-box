package com.cdwater.ticketbox.show.adapter.out.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_show_ticket_type")
public class ShowTicketTypePO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long showId;
    private String name;
    private String zone;
    private BigDecimal price;
    private Integer sortOrder;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
