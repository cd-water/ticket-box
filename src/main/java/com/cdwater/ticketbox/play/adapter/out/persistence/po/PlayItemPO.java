package com.cdwater.ticketbox.play.adapter.out.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_play_item")
public class PlayItemPO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String cover;
    private String address;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
