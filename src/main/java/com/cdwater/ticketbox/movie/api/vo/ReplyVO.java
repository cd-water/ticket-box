package com.cdwater.ticketbox.movie.api.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReplyVO {

    private Long id;
    private Long rootId;
    private Long parentId;
    private String content;
    private LocalDateTime createTime;
    private ReviewUserVO user;
    private int likeCount;
    private boolean liked;
    private ReplyToVO replyTo;
}
