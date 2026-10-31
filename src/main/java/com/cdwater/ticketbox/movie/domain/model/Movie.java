package com.cdwater.ticketbox.movie.domain.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_movie")
public class Movie {

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
}
