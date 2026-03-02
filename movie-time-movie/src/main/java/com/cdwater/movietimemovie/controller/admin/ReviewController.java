package com.cdwater.movietimemovie.controller.admin;

import com.cdwater.movietimecommon.Result;
import com.cdwater.movietimemovie.model.dto.ReviewPageDTO;
import com.cdwater.movietimemovie.model.vo.ReviewPageVO;
import com.cdwater.movietimemovie.service.ReviewService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController("adminReviewController")
@RequestMapping("/admin/review")
public class ReviewController {

    @Resource
    private ReviewService reviewService;

    @GetMapping("/page")
    public Result page(@Validated ReviewPageDTO reviewPageDTO) {
        PageInfo<ReviewPageVO> pageInfo = reviewService.page(reviewPageDTO);
        return Result.success(pageInfo);
    }

    @PutMapping("/dead/{id}")
    public Result goDead(@PathVariable Long id) {
        reviewService.goDead(id);
        return Result.success();
    }

    @PutMapping("/alive/{id}")
    public Result goAlive(@PathVariable Long id) {
        reviewService.goAlive(id);
        return Result.success();
    }
}
