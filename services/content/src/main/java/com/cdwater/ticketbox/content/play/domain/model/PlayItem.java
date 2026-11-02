package com.cdwater.ticketbox.content.play.domain.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PlayItem {

    public static final int STATUS_ON_SALE = 1;

    private Long id;
    private String title;
    private String cover;
    private String address;
    private String description;
    private BigDecimal price;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public boolean isOnSale() {
        return Integer.valueOf(STATUS_ON_SALE).equals(status);
    }
}
