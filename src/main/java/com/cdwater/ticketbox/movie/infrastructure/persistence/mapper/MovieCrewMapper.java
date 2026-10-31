package com.cdwater.ticketbox.movie.infrastructure.persistence.mapper;

import com.cdwater.ticketbox.movie.infrastructure.persistence.projection.MovieCrewRow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MovieCrewMapper {

    @Select("""
            SELECT c.movie_id, c.person_id, c.duty, c.role_name, c.sort_order,
                   p.name AS person_name, p.avatar AS person_avatar
            FROM t_movie_crew c
            JOIN t_person p ON p.id = c.person_id
            WHERE c.movie_id = #{movieId}
            ORDER BY c.duty, c.sort_order, c.person_id, c.role_name
            """)
    List<MovieCrewRow> selectCrewRowsByMovieId(@Param("movieId") Long movieId);
}
