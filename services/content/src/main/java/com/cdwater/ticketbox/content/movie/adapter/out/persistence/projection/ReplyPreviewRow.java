package com.cdwater.ticketbox.content.movie.adapter.out.persistence.projection;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReplyPreviewRow {

    private Long id;
    private Long movieId;
    private Long userId;
    private Long rootId;
    private Long parentId;
    private String content;
    private Integer likeCount;
    private Integer replyCount;
    private Integer status;
    private LocalDateTime createTime;
    private Integer rowNum;
}
