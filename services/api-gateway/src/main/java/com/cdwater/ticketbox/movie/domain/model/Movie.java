package com.cdwater.ticketbox.movie.domain.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Movie {

    public static final int STATUS_ON_SHELF = 1;

    private Long id;
    private String title;
    private String poster;
    private String synopsis;
    private Integer duration;
    private LocalDateTime releaseDate;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public boolean isOnShelf() {
        return Integer.valueOf(STATUS_ON_SHELF).equals(status);
    }

    public boolean isUpcoming(LocalDateTime now) {
        return isOnShelf() && releaseDate != null && releaseDate.isAfter(now);
    }
}
