package com.cdwater.ticketbox.content.movie.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyPreview {

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
}
