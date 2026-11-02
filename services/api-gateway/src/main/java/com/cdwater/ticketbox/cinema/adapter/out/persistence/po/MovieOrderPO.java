package com.cdwater.ticketbox.cinema.adapter.out.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_movie_order")
public class MovieOrderPO {

    @TableId(value = "order_no", type = IdType.INPUT)
    private Long orderNo;
    private Long userId;
    private Long screeningId;
    private String movieTitle;
    private String cinemaName;
    private String hallName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal totalAmount;
    /** 0-待支付 1-已支付 2-已取消（含超时） */
    private Integer status;
    private LocalDateTime expireTime;
    private LocalDateTime payTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
