package com.cdwater.ticketbox.movie.infrastructure.persistence.mapper;

import com.cdwater.ticketbox.movie.domain.model.Genre;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MovieGenreMapper {

    @Select("""
            SELECT g.id, g.name
            FROM t_movie_genre mg
            JOIN t_genre g ON g.id = mg.genre_id
            WHERE mg.movie_id = #{movieId} AND g.status = 1
            ORDER BY g.sort_order, g.id
            """)
    List<Genre> selectGenresByMovieId(@Param("movieId") Long movieId);
}
