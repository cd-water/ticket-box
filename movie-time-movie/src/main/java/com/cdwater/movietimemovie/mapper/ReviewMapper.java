package com.cdwater.movietimemovie.mapper;

import com.cdwater.movietimemovie.model.dto.ReviewPageDTO;
import com.cdwater.movietimemovie.model.entity.Review;
import com.cdwater.movietimemovie.model.entity.ReviewLike;
import com.cdwater.movietimemovie.model.vo.ReviewPageVO;
import com.cdwater.movietimemovie.model.vo.ReviewVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReviewMapper {

    List<ReviewPageVO> selectPage(ReviewPageDTO reviewPageDTO);

    void goDead(Long id);

    void goAlive(Long id);

    void insert(Review review);

    List<ReviewVO> selectByMovieIdAndParentId(Long movieId, Long parentId);

    List<ReviewVO> selectByUserId(Long userId);

    void deleteLike(Long userId, Long reviewId);

    void insertLike(Long userId, Long reviewId);

    List<ReviewLike> selectLike();
}
