package com.cdwater.ticketbox.movie.api.controller;

import com.cdwater.ticketbox.common.result.CursorResult;
import com.cdwater.ticketbox.common.result.Result;
import com.cdwater.ticketbox.movie.api.dto.ReplyListQuery;
import com.cdwater.ticketbox.movie.api.dto.ReviewCreateDTO;
import com.cdwater.ticketbox.movie.api.dto.ReviewListQuery;
import com.cdwater.ticketbox.movie.application.service.ReviewService;
import com.cdwater.ticketbox.movie.api.vo.LikeResultVO;
import com.cdwater.ticketbox.movie.api.vo.ReplyVO;
import com.cdwater.ticketbox.movie.api.vo.ReviewVO;
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

    private final ReviewService reviewService;

    @GetMapping("/movies/{movieId}/reviews")
    public Result<CursorResult<ReviewVO>> reviews(@PathVariable Long movieId,
                                                  @Valid ReviewListQuery query) {
        return Result.success(reviewService.listReviews(movieId, query));
    }

    @GetMapping("/reviews/{rootId}/replies")
    public Result<CursorResult<ReplyVO>> replies(@PathVariable Long rootId,
                                                 @Valid ReplyListQuery query) {
        return Result.success(reviewService.listReplies(rootId, query));
    }

    @PostMapping("/movies/{movieId}/reviews")
    public Result<ReviewVO> create(@PathVariable Long movieId,
                                   @Valid @RequestBody ReviewCreateDTO dto) {
        return Result.success(reviewService.createReview(movieId, dto));
    }

    @PostMapping("/reviews/{id}/reply")
    public Result<ReplyVO> reply(@PathVariable Long id,
                                 @Valid @RequestBody ReviewCreateDTO dto) {
        return Result.success(reviewService.reply(id, dto));
    }

    @PostMapping("/reviews/{id}/like")
    public Result<LikeResultVO> like(@PathVariable Long id) {
        return Result.success(reviewService.toggleLike(id));
    }
}
