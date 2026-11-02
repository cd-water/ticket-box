package com.cdwater.ticketbox.content.movie.adapter.in.rpc;

import com.cdwater.ticketbox.common.result.CursorResult;
import com.cdwater.ticketbox.content.api.ReviewApi;
import com.cdwater.ticketbox.content.api.dto.CreateReviewCommandDTO;
import com.cdwater.ticketbox.content.api.dto.LikeResultDTO;
import com.cdwater.ticketbox.content.api.dto.ReplyDTO;
import com.cdwater.ticketbox.content.api.dto.ReplyListQueryDTO;
import com.cdwater.ticketbox.content.api.dto.ReviewDTO;
import com.cdwater.ticketbox.content.api.dto.ReviewListQueryDTO;
import com.cdwater.ticketbox.content.movie.application.command.ReviewCommandService;
import com.cdwater.ticketbox.content.movie.application.query.ReviewQueryService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
@RequiredArgsConstructor
public class ReviewApiImpl implements ReviewApi {

    private final ReviewQueryService reviewQueryService;
    private final ReviewCommandService reviewCommandService;

    @Override
    public CursorResult<ReviewDTO> listReviews(long movieId, Long currentUserId,
                                               ReviewListQueryDTO query) {
        return reviewQueryService.listReviews(movieId, currentUserId, query);
    }

    @Override
    public CursorResult<ReplyDTO> listReplies(long rootId, Long currentUserId,
                                              ReplyListQueryDTO query) {
        return reviewQueryService.listReplies(rootId, currentUserId, query);
    }

    @Override
    public ReviewDTO createReview(long movieId, long userId, CreateReviewCommandDTO command) {
        return reviewCommandService.createReview(movieId, userId, command);
    }

    @Override
    public ReplyDTO reply(long reviewId, long userId, CreateReviewCommandDTO command) {
        return reviewCommandService.reply(reviewId, userId, command);
    }

    @Override
    public LikeResultDTO toggleLike(long reviewId, long userId) {
        return reviewCommandService.toggleLike(reviewId, userId);
    }
}
