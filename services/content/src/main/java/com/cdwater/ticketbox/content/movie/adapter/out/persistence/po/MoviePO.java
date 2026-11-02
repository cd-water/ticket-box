package com.cdwater.ticketbox.content.movie.adapter.out.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_movie")
public class MoviePO {

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
}
