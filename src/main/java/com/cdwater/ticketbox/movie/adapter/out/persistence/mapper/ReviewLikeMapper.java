package com.cdwater.ticketbox.movie.adapter.out.persistence.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ReviewLikeMapper {

    @Insert("INSERT IGNORE INTO t_movie_comment_like (comment_id, user_id) VALUES (#{commentId}, #{userId})")
    int insertIgnore(@Param("commentId") Long commentId, @Param("userId") Long userId);

    @Delete("DELETE FROM t_movie_comment_like WHERE comment_id = #{commentId} AND user_id = #{userId}")
    int deleteLike(@Param("commentId") Long commentId, @Param("userId") Long userId);

    @Select("""
            <script>
            SELECT comment_id FROM t_movie_comment_like
            WHERE user_id = #{userId} AND comment_id IN
            <foreach collection="commentIds" item="id" open="(" separator="," close=")">#{id}</foreach>
            </script>
            """)
    List<Long> selectLikedCommentIds(@Param("userId") Long userId,
                                     @Param("commentIds") List<Long> commentIds);
}
