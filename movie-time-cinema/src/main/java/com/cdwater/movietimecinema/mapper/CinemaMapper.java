package com.cdwater.movietimecinema.mapper;

import com.cdwater.movietimecinema.model.dto.CinemaPageDTO;
import com.cdwater.movietimecinema.model.entity.Cinema;
import com.cdwater.movietimecinema.model.vo.CinemaDetailVO;
import com.cdwater.movietimecinema.model.vo.CinemaPageVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CinemaMapper {

    void insert(Cinema cinema);

    void deleteById(Long id);

    void updateById(Cinema cinema);

    Cinema selectById(Long id);

    List<CinemaPageVO> selectPage(CinemaPageDTO cinemaPageDTO);

    Long count();

    List<Cinema> selectList();

    CinemaDetailVO selectDetail(Long cinemaId);
}
