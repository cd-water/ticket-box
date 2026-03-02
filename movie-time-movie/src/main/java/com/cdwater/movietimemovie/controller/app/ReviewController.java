package com.cdwater.movietimemovie.controller.app;

import com.cdwater.movietimecommon.Result;
import com.cdwater.movietimecommon.annotations.SkipAppInterceptor;
import com.cdwater.movietimemovie.model.dto.ReviewAddDTO;
import com.cdwater.movietimemovie.model.vo.ReviewVO;
import com.cdwater.movietimemovie.service.ReviewService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("appReviewController")
@RequestMapping("/app/review")
public class ReviewController {

    @Resource
    private ReviewService reviewService;

    @PostMapping("/add")
    public Result add(@Valid @RequestBody ReviewAddDTO reviewAddDTO) {
        reviewService.add(reviewAddDTO);
        return Result.success();
    }

    @SkipAppInterceptor
    @GetMapping("/movie/{movieId}")
    public Result showMovieReview(@PathVariable("movieId") Long movieId,
                                  @RequestParam Long parentId,
                                  @RequestHeader("Authorization") String authHeader) {
        List<ReviewVO> list = reviewService.showMovieReview(movieId, parentId, authHeader);
        return Result.success(list);
    }

    @SkipAppInterceptor
    @GetMapping("/user/{userId}")
    public Result showUserReview(@PathVariable("userId") Long userId,
                                 @RequestHeader("Authorization") String authHeader) {
        List<ReviewVO> list = reviewService.showUserReview(userId, authHeader);
        return Result.success(list);
    }

    @PutMapping("/like/{id}")
    public Result like(@PathVariable("id") Long reviewId) {
        reviewService.like(reviewId);
        return Result.success();
    }
}
