package com.cdwater.ticketbox.content.movie.domain.repository;

import com.cdwater.ticketbox.content.movie.domain.model.Review;
import com.cdwater.ticketbox.content.movie.domain.model.ReplyPreview;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ReviewRepository {

    List<Review> findRootHot(Long movieId, Integer likeCount, Long cursorId, int limit);

    List<Review> findRootLatest(Long movieId, LocalDateTime cursorTime, Long cursorId, int limit);

    List<Review> findReplies(Long rootId, LocalDateTime cursorTime, Long cursorId, int limit);

    List<ReplyPreview> findReplyPreviews(List<Long> rootIds);

    Optional<Review> findById(Long id);

    Review findByIdForUpdate(Long id);

    List<Review> findByIds(Collection<Long> ids);

    void insert(Review comment);

    void incrementReplyCount(Long id);

    void incrementLikeCount(Long id);

    void decrementLikeCount(Long id);

    boolean insertLikeIgnore(Long commentId, Long userId);

    boolean deleteLike(Long commentId, Long userId);

    Set<Long> findLikedCommentIds(Long userId, Collection<Long> commentIds);

    long countByMovieId(Long movieId);
}
