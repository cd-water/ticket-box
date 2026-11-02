package com.cdwater.ticketbox.movie.adapter.in.web;

import com.cdwater.ticketbox.common.result.CursorResult;
import com.cdwater.ticketbox.common.security.CurrentUser;
import com.cdwater.ticketbox.common.result.Result;
import com.cdwater.ticketbox.movie.application.command.CreateReviewCommand;
import com.cdwater.ticketbox.movie.application.command.ReviewCommandService;
import com.cdwater.ticketbox.movie.application.query.ReplyListQuery;
import com.cdwater.ticketbox.movie.application.query.ReviewListQuery;
import com.cdwater.ticketbox.movie.application.query.ReviewQueryService;
import com.cdwater.ticketbox.movie.application.query.view.LikeResultView;
import com.cdwater.ticketbox.movie.application.query.view.ReplyView;
import com.cdwater.ticketbox.movie.application.query.view.ReviewView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    private final ReviewQueryService reviewQueryService;
    private final ReviewCommandService reviewCommandService;
    private final CurrentUser currentUser;

    @GetMapping("/movies/{movieId}/reviews")
    public Result<CursorResult<ReviewView>> reviews(@PathVariable Long movieId,
                                                  @Valid ReviewListQuery query) {
        return Result.success(reviewQueryService.listReviews(
                movieId, currentUser.id(), query));
    }

    @GetMapping("/reviews/{rootId}/replies")
    public Result<CursorResult<ReplyView>> replies(@PathVariable Long rootId,
                                                 @Valid ReplyListQuery query) {
        return Result.success(reviewQueryService.listReplies(
                rootId, currentUser.id(), query));
    }

    @PostMapping("/movies/{movieId}/reviews")
    public Result<ReviewView> create(@PathVariable Long movieId,
                                   @Valid @RequestBody CreateReviewCommand command) {
        return Result.success(reviewCommandService.createReview(
                movieId, currentUser.id(), command));
    }

    @PostMapping("/reviews/{id}/reply")
    public Result<ReplyView> reply(@PathVariable Long id,
                                 @Valid @RequestBody CreateReviewCommand command) {
        return Result.success(reviewCommandService.reply(
                id, currentUser.id(), command));
    }

    @PostMapping("/reviews/{id}/like")
    public Result<LikeResultView> like(@PathVariable Long id) {
        return Result.success(reviewCommandService.toggleLike(
                id, currentUser.id()));
    }
}
