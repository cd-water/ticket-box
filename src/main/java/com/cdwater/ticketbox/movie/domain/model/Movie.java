package com.cdwater.ticketbox.movie.domain.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_movie")
public class Movie {

    public static final int STATUS_ON_SHELF = 1;

    @TableId(type = IdType.AUTO)
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
