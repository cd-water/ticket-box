package com.cdwater.ticketbox.content.movie.adapter.out.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdwater.ticketbox.content.movie.adapter.out.persistence.po.MoviePO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MovieMapper extends BaseMapper<MoviePO> {
}
