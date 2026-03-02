package com.cdwater.movietimeorder.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderListVO {

    /**
     * 订单相关
     */
    private Long orderNo;
    private String seatInfo;
    private Integer ticketCount;
    private BigDecimal amount;
    private Integer status;

    /**
     * 电影相关
     */
    private Long movieId;
    private String movieName;
    private String movieCover;

    /**
     * 影院相关
     */
    private Long cinemaId;
    private String cinemaName;

    /**
     * 场次相关
     */
    private String hallName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
