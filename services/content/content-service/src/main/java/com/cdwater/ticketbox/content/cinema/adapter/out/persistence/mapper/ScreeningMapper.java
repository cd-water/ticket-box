package com.cdwater.ticketbox.content.cinema.adapter.out.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdwater.ticketbox.content.cinema.adapter.out.persistence.po.ScreeningPO;
import com.cdwater.ticketbox.content.cinema.adapter.out.persistence.projection.ScreeningRow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ScreeningMapper extends BaseMapper<ScreeningPO> {

    @Select("""
            SELECT DISTINCT movie_id
            FROM t_screening
            WHERE cinema_id = #{cinemaId} AND status = 1 AND start_time >= NOW()
            """)
    List<Long> selectFutureMovieIds(@Param("cinemaId") Long cinemaId);

    @Select("""
            SELECT s.id, s.hall_id, h.name AS hall_name,
                   s.start_time, s.end_time, s.price
            FROM t_screening s
            JOIN t_hall h ON h.id = s.hall_id
            WHERE s.cinema_id = #{cinemaId} AND s.movie_id = #{movieId}
              AND s.status = 1 AND s.start_time >= NOW()
            ORDER BY s.start_time
            """)
    List<ScreeningRow> selectFutureWithHall(@Param("cinemaId") Long cinemaId,
                                            @Param("movieId") Long movieId);
}
