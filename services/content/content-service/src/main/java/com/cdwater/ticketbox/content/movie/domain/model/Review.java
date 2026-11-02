package com.cdwater.ticketbox.content.movie.domain.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Review {

    public static final long ROOT_ID = 0L;
    public static final int STATUS_NORMAL = 1;

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
    private LocalDateTime updateTime;

    public static Review createRoot(Long movieId, Long userId, String content) {
        return create(movieId, userId, ROOT_ID, ROOT_ID, content);
    }

    public static Review createReply(Long movieId, Long userId, Long rootId, Long parentId, String content) {
        return create(movieId, userId, rootId, parentId, content);
    }

    public boolean isRoot() {
        return Long.valueOf(ROOT_ID).equals(rootId);
    }

    public boolean isVisible() {
        return Integer.valueOf(STATUS_NORMAL).equals(status);
    }

    private static Review create(Long movieId, Long userId, Long rootId, Long parentId, String content) {
        Review review = new Review();
        review.setMovieId(movieId);
        review.setUserId(userId);
        review.setRootId(rootId);
        review.setParentId(parentId);
        review.setContent(content.trim());
        review.setLikeCount(0);
        review.setReplyCount(0);
        review.setStatus(STATUS_NORMAL);
        return review;
    }
}
