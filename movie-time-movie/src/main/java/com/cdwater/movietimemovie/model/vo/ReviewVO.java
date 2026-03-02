package com.cdwater.movietimemovie.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewVO {
    /**
     * 影评相关
     */
    private Long id;
    private String content;
    private Integer likeCount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    private Boolean hasLiked;

    /**
     * 用户相关
     */
    private Long userId;
    private String userNickname;
    private String userAvatar;

    /**
     * 电影相关
     */
    private Long movieId;
    private String movieName;
    private String movieCover;
}
