package com.cdwater.ticketbox.content.api.dto;

import java.io.Serializable;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReplyDTO  implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long rootId;
    private Long parentId;
    private String content;
    private LocalDateTime createTime;
    private ReviewUserDTO user;
    private int likeCount;
    private boolean liked;
    private ReplyToDTO replyTo;
}
