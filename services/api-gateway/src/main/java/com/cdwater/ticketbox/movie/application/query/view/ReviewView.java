package com.cdwater.ticketbox.movie.application.query.view;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ReviewView {

    private Long id;
    private String content;
    private LocalDateTime createTime;
    private ReviewUserView user;
    private int likeCount;
    private boolean liked;
    private int replyCount;
    private List<ReplyView> replies = new ArrayList<>();
}
