package com.cdwater.movietimemovie.mapper;

import com.cdwater.movietimemovie.model.dto.MoviePageDTO;
import com.cdwater.movietimemovie.model.entity.Movie;
import com.cdwater.movietimemovie.model.vo.MoviePageVO;
import com.cdwater.movietimemovie.model.vo.MovieShowingVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MovieMapper {

    void insert(Movie movie);

    void updateById(Movie movie);

    Movie selectById(Long id);

    List<MoviePageVO> selectPage(MoviePageDTO moviePageDTO);

    List<MovieShowingVO> selectMapByStatus(Integer status);

    Long count();

    List<Movie> selectListByStatus(Integer status);
}
