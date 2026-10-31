package com.cdwater.ticketbox.cinema.adapter.out.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdwater.ticketbox.cinema.domain.model.Cinema;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CinemaMapper extends BaseMapper<Cinema> {
}
