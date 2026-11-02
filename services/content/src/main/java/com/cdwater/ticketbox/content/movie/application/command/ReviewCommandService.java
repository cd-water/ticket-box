package com.cdwater.ticketbox.content.movie.application.command;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.ResultCode;
import com.cdwater.ticketbox.content.movie.application.query.support.ReviewViewAssembler;
import com.cdwater.ticketbox.content.api.dto.CreateReviewCommandDTO;
import com.cdwater.ticketbox.content.api.dto.LikeResultDTO;
import com.cdwater.ticketbox.content.api.dto.ReplyDTO;
import com.cdwater.ticketbox.content.api.dto.ReviewDTO;
import com.cdwater.ticketbox.content.movie.domain.model.Review;
import com.cdwater.ticketbox.content.movie.domain.repository.MovieRepository;
import com.cdwater.ticketbox.content.movie.domain.repository.ReviewRepository;
import com.cdwater.ticketbox.content.movie.application.port.UserInfo;
import com.cdwater.ticketbox.content.movie.application.port.UserInfoPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReviewCommandService {

    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;
    private final UserInfoPort userInfoPort;
    private final ReviewViewAssembler assembler;

    @Transactional
    public ReviewDTO createReview(Long movieId, Long userId, CreateReviewCommandDTO command) {
        requireMovie(movieId);
        Review comment = Review.createRoot(movieId, userId, command.getContent());
        reviewRepository.insert(comment);

        Review saved = reviewRepository.findById(comment.getId())
                .orElseThrow(() -> new BizException(ResultCode.INTERNAL_SERVER_ERROR, "影评写入失败"));
        UserInfo user = userInfoPort.findById(userId).orElse(null);
        return assembler.baseReview(saved, user, false);
    }

    @Transactional
    public ReplyDTO reply(Long targetId, Long userId, CreateReviewCommandDTO command) {
        Review target = reviewRepository.findById(targetId).orElse(null);
        if (target == null || !target.isVisible()) {
            throw new BizException(ResultCode.NOT_FOUND, "影评不存在或不可见");
        }
        requireMovie(target.getMovieId());
        Long rootId = target.isRoot() ? target.getId() : target.getRootId();
        Review root = reviewRepository.findById(rootId).orElse(null);
        if (root == null || !root.isVisible() || !root.isRoot()) {
            throw new BizException(ResultCode.NOT_FOUND, "影评不存在或不可见");
        }

        Review reply = Review.createReply(target.getMovieId(), userId, rootId, target.getId(), command.getContent());
        reviewRepository.insert(reply);
        reviewRepository.incrementReplyCount(rootId);

        Review saved = reviewRepository.findById(reply.getId())
                .orElseThrow(() -> new BizException(ResultCode.INTERNAL_SERVER_ERROR, "回复写入失败"));
        Set<Long> userIds = new HashSet<>();
        userIds.add(userId);
        userIds.add(target.getUserId());
        Map<Long, UserInfo> users = userInfoPort.findByIds(userIds);
        Map<Long, Review> parents = Map.of(target.getId(), target);
        return assembler.toReplyView(saved, users, parents, Set.of());
    }

    @Transactional
    public LikeResultDTO toggleLike(Long commentId, Long userId) {
        // 先锁父评论行，串行化同一评论的并发点赞，避免 INSERT IGNORE 唯一键共享锁升级死锁
        Review comment = reviewRepository.findByIdForUpdate(commentId);
        if (comment == null || !comment.isVisible()) {
            throw new BizException(ResultCode.NOT_FOUND, "影评不存在或不可见");
        }
        boolean liked;
        if (reviewRepository.insertLikeIgnore(commentId, userId)) {
            reviewRepository.incrementLikeCount(commentId);
            liked = true;
        } else {
            if (reviewRepository.deleteLike(commentId, userId)) {
                reviewRepository.decrementLikeCount(commentId);
            }
            liked = false;
        }
        Review updated = reviewRepository.findById(commentId)
                .orElseThrow(() -> new BizException(ResultCode.INTERNAL_SERVER_ERROR, "影评不存在"));
        return new LikeResultDTO(liked, updated.getLikeCount());
    }

    private void requireMovie(Long movieId) {
        movieRepository.findById(movieId)
                .filter(movie -> movie.getStatus() != null && movie.getStatus() == 1)
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "电影不存在或已下架"));
    }
}