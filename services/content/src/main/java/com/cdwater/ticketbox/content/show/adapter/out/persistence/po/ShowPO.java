package com.cdwater.ticketbox.content.show.adapter.out.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_show")
public class ShowPO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String poster;
    private String description;
    private Integer duration;
    private String venueName;
    private String venueAddress;
    private LocalDateTime startTime;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
