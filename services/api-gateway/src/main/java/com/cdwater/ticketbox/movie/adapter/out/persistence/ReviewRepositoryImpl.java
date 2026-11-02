package com.cdwater.ticketbox.movie.adapter.out.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cdwater.ticketbox.movie.adapter.out.persistence.converter.ReviewConverter;
import com.cdwater.ticketbox.movie.adapter.out.persistence.converter.ReviewProjectionConverter;
import com.cdwater.ticketbox.movie.adapter.out.persistence.mapper.ReviewLikeMapper;
import com.cdwater.ticketbox.movie.adapter.out.persistence.mapper.ReviewMapper;
import com.cdwater.ticketbox.movie.adapter.out.persistence.po.ReviewPO;
import com.cdwater.ticketbox.movie.domain.model.ReplyPreview;
import com.cdwater.ticketbox.movie.domain.model.Review;
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

    private final ReviewMapper reviewMapper;
    private final ReviewLikeMapper likeMapper;
    private final ReviewProjectionConverter reviewProjectionConverter;
    private final ReviewConverter reviewConverter;

    @Override
    public List<Review> findRootHot(Long movieId, Integer likeCount, Long cursorId, int limit) {
        return reviewConverter.toDomain(reviewMapper.selectRootHot(movieId, likeCount, cursorId, limit));
    }

    @Override
    public List<Review> findRootLatest(Long movieId, LocalDateTime cursorTime, Long cursorId, int limit) {
        return reviewConverter.toDomain(reviewMapper.selectRootLatest(movieId, cursorTime, cursorId, limit));
    }

    @Override
    public List<Review> findReplies(Long rootId, LocalDateTime cursorTime, Long cursorId, int limit) {
        return reviewConverter.toDomain(reviewMapper.selectReplies(rootId, cursorTime, cursorId, limit));
    }

    @Override
    public List<ReplyPreview> findReplyPreviews(List<Long> rootIds) {
        return reviewMapper.selectReplyPreviews(rootIds).stream()
                .map(reviewProjectionConverter::toDomain)
                .toList();
    }

    @Override
    public Optional<Review> findById(Long id) {
        return Optional.ofNullable(reviewMapper.selectById(id))
                .map(reviewConverter::toDomain);
    }

    @Override
    public Review findByIdForUpdate(Long id) {
        return reviewConverter.toDomain(reviewMapper.selectByIdForUpdate(id));
    }

    @Override
    public List<Review> findByIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return reviewConverter.toDomain(reviewMapper.selectByIds(ids));
    }

    @Override
    public void insert(Review comment) {
        ReviewPO po = reviewConverter.toPO(comment);
        reviewMapper.insert(po);
        comment.setId(po.getId());
    }

    @Override
    public void incrementReplyCount(Long id) {
        reviewMapper.incrementReplyCount(id);
    }

    @Override
    public void incrementLikeCount(Long id) {
        reviewMapper.incrementLikeCount(id);
    }

    @Override
    public void decrementLikeCount(Long id) {
        reviewMapper.decrementLikeCount(id);
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
        return reviewMapper.selectCount(new LambdaQueryWrapper<ReviewPO>()
                .eq(ReviewPO::getMovieId, movieId)
                .eq(ReviewPO::getStatus, Review.STATUS_NORMAL));
    }
}
