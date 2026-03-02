package com.cdwater.movietimemovie.service;

import com.cdwater.movietimemovie.model.dto.ReviewAddDTO;
import com.cdwater.movietimemovie.model.dto.ReviewPageDTO;
import com.cdwater.movietimemovie.model.vo.ReviewPageVO;
import com.cdwater.movietimemovie.model.vo.ReviewVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ReviewService {

    PageInfo<ReviewPageVO> page(ReviewPageDTO reviewPageDTO);

    void goDead(Long id);

    void goAlive(Long id);

    void add(ReviewAddDTO reviewAddDTO);

    List<ReviewVO> showMovieReview(Long movieId, Long parentId, String authHeader);

    List<ReviewVO> showUserReview(Long userId, String authHeader);

    void like(Long reviewId);
}
