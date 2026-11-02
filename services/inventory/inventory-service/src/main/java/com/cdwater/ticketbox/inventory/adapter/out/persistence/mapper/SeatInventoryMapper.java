package com.cdwater.ticketbox.inventory.adapter.out.persistence.mapper;

import com.cdwater.ticketbox.inventory.adapter.out.persistence.projection.SeatInventoryCountRow;
import com.cdwater.ticketbox.inventory.domain.model.SeatSnapshot;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Mapper
public interface SeatInventoryMapper {

    @Select("""
            SELECT s.id AS seat_id, s.row_num, s.col_num, ss.status
            FROM t_seat s
            JOIN t_screening_seat ss ON ss.seat_id = s.id
            WHERE ss.screening_id = #{screeningId}
            ORDER BY s.row_num, s.col_num
            """)
    List<SeatSnapshot> selectSeatMap(@Param("screeningId") Long screeningId);

    @Select("""
            <script>
            SELECT s.id AS seat_id, s.row_num, s.col_num,
                   ss.status, ss.lock_order_no, ss.lock_expire_time
            FROM t_screening_seat ss
            JOIN t_seat s ON s.id = ss.seat_id
            WHERE ss.screening_id = #{screeningId} AND ss.seat_id IN
            <foreach collection="seatIds" item="id" open="(" separator="," close=")">#{id}</foreach>
            ORDER BY ss.seat_id
            FOR UPDATE
            </script>
            """)
    List<SeatSnapshot> selectSeatsForUpdate(@Param("screeningId") Long screeningId,
                                            @Param("seatIds") Collection<Long> seatIds);

    @Update("""
            <script>
            UPDATE t_screening_seat
            SET status = 1, lock_order_no = #{orderNo}, lock_expire_time = #{expireTime}
            WHERE screening_id = #{screeningId} AND seat_id IN
            <foreach collection="seatIds" item="id" open="(" separator="," close=")">#{id}</foreach>
            </script>
            """)
    int updateLocks(@Param("screeningId") Long screeningId,
                    @Param("seatIds") Collection<Long> seatIds,
                    @Param("orderNo") Long orderNo,
                    @Param("expireTime") LocalDateTime expireTime);

    @Update("""
            UPDATE t_screening_seat
            SET status = 0, lock_order_no = NULL, lock_expire_time = NULL
            WHERE lock_order_no = #{orderNo} AND status = 1
            """)
    int releaseByOrder(@Param("orderNo") Long orderNo);

    @Update("""
            UPDATE t_screening_seat SET status = 2, lock_expire_time = NULL
            WHERE lock_order_no = #{orderNo} AND status = 1
            """)
    int markSoldByOrder(@Param("orderNo") Long orderNo);

    @Update("""
            UPDATE t_screening_seat SET status = 0, lock_order_no = NULL
            WHERE lock_order_no = #{orderNo} AND status = 2
            """)
    int rollbackSale(@Param("orderNo") Long orderNo);

    @Select("""
            <script>
            SELECT screening_id, COUNT(*) AS available_count
            FROM t_screening_seat
            WHERE status = 0 AND screening_id IN
            <foreach collection="screeningIds" item="id" open="(" separator="," close=")">#{id}</foreach>
            GROUP BY screening_id
            </script>
            """)
    List<SeatInventoryCountRow> selectAvailableCounts(@Param("screeningIds") Collection<Long> screeningIds);

    @Update("""
            UPDATE t_screening_seat
            SET status = 0, lock_order_no = NULL, lock_expire_time = NULL
            WHERE screening_id = #{screeningId} AND status = 1
              AND lock_expire_time < #{now}
            """)
    int releaseExpiredByScreening(@Param("screeningId") Long screeningId,
                                  @Param("now") LocalDateTime now);
}
