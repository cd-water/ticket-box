package com.cdwater.ticketbox.movie.api.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ReviewVO {

    private Long id;
    private String content;
    private LocalDateTime createTime;
    private ReviewUserVO user;
    private int likeCount;
    private boolean liked;
    private int replyCount;
    private List<ReplyVO> replies = new ArrayList<>();
}
