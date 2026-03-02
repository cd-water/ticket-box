package com.cdwater.movietimecinema.mapper;

import com.cdwater.movietimecinema.model.dto.ScreeningPageDTO;
import com.cdwater.movietimecinema.model.entity.Screening;
import com.cdwater.movietimecinema.model.vo.ScreeningListVO;
import com.cdwater.movietimecinema.model.vo.ScreeningPageVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ScreeningMapper {

    void insert(Screening screening);

    void delete(Long id);

    void updateById(Screening screening);

    Screening selectById(Long id);

    List<ScreeningPageVO> selectPage(ScreeningPageDTO screeningPageDTO);

    List<ScreeningListVO> selectLatestByCinemaIdAndMovieId(Long cinemaId, Long movieId);
}
