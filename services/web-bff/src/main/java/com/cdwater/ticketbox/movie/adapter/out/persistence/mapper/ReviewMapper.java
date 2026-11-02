package com.cdwater.ticketbox.movie.adapter.out.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdwater.ticketbox.movie.adapter.out.persistence.projection.ReplyPreviewRow;
import com.cdwater.ticketbox.movie.adapter.out.persistence.po.ReviewPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ReviewMapper extends BaseMapper<ReviewPO> {

    @Select("""
            <script>
            SELECT * FROM t_movie_comment
            WHERE movie_id = #{movieId} AND root_id = 0 AND status = 1
            <if test="cursorId != null">
              AND (like_count &lt; #{cursorLikeCount}
                   OR (like_count = #{cursorLikeCount} AND id &lt; #{cursorId}))
            </if>
            ORDER BY like_count DESC, id DESC
            LIMIT #{limit}
            </script>
            """)
    List<ReviewPO> selectRootHot(@Param("movieId") Long movieId,
                                     @Param("cursorLikeCount") Integer cursorLikeCount,
                                     @Param("cursorId") Long cursorId,
                                     @Param("limit") int limit);

    @Select("""
            <script>
            SELECT * FROM t_movie_comment
            WHERE movie_id = #{movieId} AND root_id = 0 AND status = 1
            <if test="cursorId != null">
              AND (create_time &lt; #{cursorTime}
                   OR (create_time = #{cursorTime} AND id &lt; #{cursorId}))
            </if>
            ORDER BY create_time DESC, id DESC
            LIMIT #{limit}
            </script>
            """)
    List<ReviewPO> selectRootLatest(@Param("movieId") Long movieId,
                                        @Param("cursorTime") LocalDateTime cursorTime,
                                        @Param("cursorId") Long cursorId,
                                        @Param("limit") int limit);

    @Select("""
            <script>
            SELECT * FROM t_movie_comment
            WHERE root_id = #{rootId} AND status = 1
            <if test="cursorId != null">
              AND (create_time &gt; #{cursorTime}
                   OR (create_time = #{cursorTime} AND id &gt; #{cursorId}))
            </if>
            ORDER BY create_time ASC, id ASC
            LIMIT #{limit}
            </script>
            """)
    List<ReviewPO> selectReplies(@Param("rootId") Long rootId,
                                     @Param("cursorTime") LocalDateTime cursorTime,
                                     @Param("cursorId") Long cursorId,
                                     @Param("limit") int limit);

    @Select("""
            <script>
            SELECT * FROM (
                SELECT c.*,
                       ROW_NUMBER() OVER (PARTITION BY c.root_id ORDER BY c.create_time, c.id) AS row_num
                FROM t_movie_comment c
                WHERE c.status = 1 AND c.root_id IN
                <foreach collection="rootIds" item="id" open="(" separator="," close=")">#{id}</foreach>
            ) t
            WHERE t.row_num &lt;= 2
            ORDER BY t.root_id, t.row_num
            </script>
            """)
    List<ReplyPreviewRow> selectReplyPreviews(@Param("rootIds") List<Long> rootIds);

    @Select("SELECT * FROM t_movie_comment WHERE id = #{id} FOR UPDATE")
    ReviewPO selectByIdForUpdate(@Param("id") Long id);

    @Update("UPDATE t_movie_comment SET reply_count = reply_count + 1 WHERE id = #{id}")
    int incrementReplyCount(@Param("id") Long id);

    @Update("UPDATE t_movie_comment SET like_count = like_count + 1 WHERE id = #{id}")
    int incrementLikeCount(@Param("id") Long id);

    @Update("UPDATE t_movie_comment SET like_count = GREATEST(like_count - 1, 0) WHERE id = #{id}")
    int decrementLikeCount(@Param("id") Long id);
}
