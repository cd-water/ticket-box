package com.cdwater.ticketbox.movie.adapter.out.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cdwater.ticketbox.movie.adapter.out.persistence.mapper.ReviewLikeMapper;
import com.cdwater.ticketbox.movie.adapter.out.persistence.mapper.ReviewMapper;
import com.cdwater.ticketbox.movie.domain.model.Review;
import com.cdwater.ticketbox.movie.domain.model.ReplyPreview;
import com.cdwater.ticketbox.movie.domain.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {

    private final ReviewMapper commentMapper;
    private final ReviewLikeMapper likeMapper;
    private final ReviewProjectionMapper reviewProjectionMapper;

    @Override
    public List<Review> findRootHot(Long movieId, Integer likeCount, Long cursorId, int limit) {
        return commentMapper.selectRootHot(movieId, likeCount, cursorId, limit);
    }

    @Override
    public List<Review> findRootLatest(Long movieId, LocalDateTime cursorTime, Long cursorId, int limit) {
        return commentMapper.selectRootLatest(movieId, cursorTime, cursorId, limit);
    }

    @Override
    public List<Review> findReplies(Long rootId, LocalDateTime cursorTime, Long cursorId, int limit) {
        return commentMapper.selectReplies(rootId, cursorTime, cursorId, limit);
    }

    @Override
    public List<ReplyPreview> findReplyPreviews(List<Long> rootIds) {
        return commentMapper.selectReplyPreviews(rootIds).stream()
                .map(reviewProjectionMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Review> findById(Long id) {
        return Optional.ofNullable(commentMapper.selectById(id));
    }

    @Override
    public Review findByIdForUpdate(Long id) {
        return commentMapper.selectByIdForUpdate(id);
    }

    @Override
    public List<Review> findByIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return commentMapper.selectByIds(ids);
    }

    @Override
    public void insert(Review comment) {
        commentMapper.insert(comment);
    }

    @Override
    public void incrementReplyCount(Long id) {
        commentMapper.incrementReplyCount(id);
    }

    @Override
    public void incrementLikeCount(Long id) {
        commentMapper.incrementLikeCount(id);
    }

    @Override
    public void decrementLikeCount(Long id) {
        commentMapper.decrementLikeCount(id);
    }

    @Override
    public boolean insertLikeIgnore(Long commentId, Long userId) {
        return likeMapper.insertIgnore(commentId, userId) == 1;
    }

    @Override
    public boolean deleteLike(Long commentId, Long userId) {
        return likeMapper.deleteLike(commentId, userId) == 1;
    }

    @Override
    public Set<Long> findLikedCommentIds(Long userId, Collection<Long> commentIds) {
        if (commentIds.isEmpty()) {
            return Set.of();
        }
        return new HashSet<>(likeMapper.selectLikedCommentIds(userId, new ArrayList<>(commentIds)));
    }

    @Override
    public long countByMovieId(Long movieId) {
        return commentMapper.selectCount(new LambdaQueryWrapper<Review>()
                .eq(Review::getMovieId, movieId)
                .eq(Review::getStatus, Review.STATUS_NORMAL));
    }
}
