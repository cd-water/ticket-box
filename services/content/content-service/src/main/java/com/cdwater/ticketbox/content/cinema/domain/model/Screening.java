package com.cdwater.ticketbox.content.cinema.domain.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Screening {

    private Long id;
    private Long cinemaId;
    private Long hallId;
    private Long movieId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal price;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;


    /** 场次可展示（未取消）。 */
    public boolean isOnSale() {
        return Integer.valueOf(1).equals(status);
    }

    /** 场次可下单（未取消且未开演）。 */
    public boolean isBookable(java.time.LocalDateTime now) {
        return isOnSale() && startTime != null && startTime.isAfter(now);
    }
}
