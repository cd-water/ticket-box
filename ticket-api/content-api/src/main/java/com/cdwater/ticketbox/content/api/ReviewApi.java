package com.cdwater.ticketbox.content.api;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.CursorResult;
import com.cdwater.ticketbox.content.api.dto.CreateReviewCommandDTO;
import com.cdwater.ticketbox.content.api.dto.LikeResultDTO;
import com.cdwater.ticketbox.content.api.dto.ReplyDTO;
import com.cdwater.ticketbox.content.api.dto.ReplyListQueryDTO;
import com.cdwater.ticketbox.content.api.dto.ReviewDTO;
import com.cdwater.ticketbox.content.api.dto.ReviewListQueryDTO;

public interface ReviewApi {

    CursorResult<ReviewDTO> listReviews(long movieId, Long currentUserId, ReviewListQueryDTO query)
            throws BizException;

    CursorResult<ReplyDTO> listReplies(long rootId, Long currentUserId, ReplyListQueryDTO query)
            throws BizException;

    ReviewDTO createReview(long movieId, long userId, CreateReviewCommandDTO command)
            throws BizException;

    ReplyDTO reply(long reviewId, long userId, CreateReviewCommandDTO command)
            throws BizException;

    LikeResultDTO toggleLike(long reviewId, long userId) throws BizException;
}
