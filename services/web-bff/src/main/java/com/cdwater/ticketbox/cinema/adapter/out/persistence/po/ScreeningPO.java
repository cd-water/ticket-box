package com.cdwater.ticketbox.cinema.adapter.out.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_screening")
public class ScreeningPO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long cinemaId;
    private Long hallId;
    private Long movieId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private java.math.BigDecimal price;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
