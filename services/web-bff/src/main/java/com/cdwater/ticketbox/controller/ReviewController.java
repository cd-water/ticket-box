package com.cdwater.ticketbox.controller;

import com.cdwater.ticketbox.common.result.CursorResult;
import com.cdwater.ticketbox.common.result.Result;
import com.cdwater.ticketbox.common.security.CurrentUser;
import com.cdwater.ticketbox.content.api.ReviewApi;
import com.cdwater.ticketbox.content.api.dto.CreateReviewCommandDTO;
import com.cdwater.ticketbox.content.api.dto.LikeResultDTO;
import com.cdwater.ticketbox.content.api.dto.ReplyDTO;
import com.cdwater.ticketbox.content.api.dto.ReplyListQueryDTO;
import com.cdwater.ticketbox.content.api.dto.ReviewDTO;
import com.cdwater.ticketbox.content.api.dto.ReviewListQueryDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

    @DubboReference(check = false, timeout = 3000)
    private ReviewApi reviewApi;

    private final CurrentUser currentUser;

    @GetMapping("/movies/{movieId}/reviews")
    public Result<CursorResult<ReviewDTO>> listReviews(@PathVariable Long movieId,
                                                  @Valid ReviewListQueryDTO query) {
        return Result.success(reviewApi.listReviews(movieId, currentUser.id(), query));
    }

    @GetMapping("/reviews/{rootId}/replies")
    public Result<CursorResult<ReplyDTO>> listReplies(@PathVariable Long rootId,
                                                 @Valid ReplyListQueryDTO query) {
        return Result.success(reviewApi.listReplies(rootId, currentUser.id(), query));
    }

    @PostMapping("/movies/{movieId}/reviews")
    public Result<ReviewDTO> create(@PathVariable Long movieId,
                                   @Valid @RequestBody CreateReviewCommandDTO command) {
        return Result.success(reviewApi.createReview(movieId, currentUser.id(), command));
    }

    @PostMapping("/reviews/{id}/reply")
    public Result<ReplyDTO> reply(@PathVariable Long id,
                                 @Valid @RequestBody CreateReviewCommandDTO command) {
        return Result.success(reviewApi.reply(id, currentUser.id(), command));
    }

    @PostMapping("/reviews/{id}/like")
    public Result<LikeResultDTO> like(@PathVariable Long id) {
        return Result.success(reviewApi.toggleLike(id, currentUser.id()));
    }
}
