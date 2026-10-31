package com.cdwater.ticketbox.cinema.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdwater.ticketbox.cinema.domain.model.Hall;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HallMapper extends BaseMapper<Hall> {
}
