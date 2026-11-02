package com.cdwater.ticketbox.content.api.dto;

import java.io.Serializable;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ReviewDTO  implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String content;
    private LocalDateTime createTime;
    private ReviewUserDTO user;
    private int likeCount;
    private boolean liked;
    private int replyCount;
    private List<ReplyDTO> replies = new ArrayList<>();
}
