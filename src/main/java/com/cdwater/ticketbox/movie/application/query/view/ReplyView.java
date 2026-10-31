package com.cdwater.ticketbox.movie.application.query.view;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReplyView {

    private Long id;
    private Long rootId;
    private Long parentId;
    private String content;
    private LocalDateTime createTime;
    private ReviewUserView user;
    private int likeCount;
    private boolean liked;
    private ReplyToView replyTo;
}
