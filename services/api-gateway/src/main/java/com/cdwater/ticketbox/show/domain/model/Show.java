package com.cdwater.ticketbox.show.domain.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Show {

    public static final int STATUS_ON_SALE = 1;

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

    public boolean isOnSale() {
        return Integer.valueOf(STATUS_ON_SALE).equals(status);
    }

    public boolean isUpcoming(LocalDateTime now) {
        return isOnSale() && startTime != null && startTime.isAfter(now);
    }
}
