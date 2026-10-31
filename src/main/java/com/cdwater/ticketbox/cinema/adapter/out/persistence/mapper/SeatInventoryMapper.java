package com.cdwater.ticketbox.cinema.adapter.out.persistence.mapper;

import com.cdwater.ticketbox.cinema.domain.model.SeatSnapshot;
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
                   ss.status, ss.lock_order_id, ss.lock_expire_time
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
            SET status = 1, lock_order_id = #{orderId}, lock_expire_time = #{expireTime}
            WHERE screening_id = #{screeningId} AND seat_id IN
            <foreach collection="seatIds" item="id" open="(" separator="," close=")">#{id}</foreach>
            </script>
            """)
    int updateLocks(@Param("screeningId") Long screeningId,
                    @Param("seatIds") Collection<Long> seatIds,
                    @Param("orderId") Long orderId,
                    @Param("expireTime") LocalDateTime expireTime);

    @Update("""
            UPDATE t_screening_seat
            SET status = 0, lock_order_id = NULL, lock_expire_time = NULL
            WHERE lock_order_id = #{orderId} AND status = 1
            """)
    int releaseByOrder(@Param("orderId") Long orderId);

    @Update("""
            UPDATE t_screening_seat
            SET status = 0, lock_order_id = NULL, lock_expire_time = NULL
            WHERE screening_id = #{screeningId} AND status = 1
              AND lock_expire_time < #{now}
            """)
    int releaseExpiredByScreening(@Param("screeningId") Long screeningId,
                                  @Param("now") LocalDateTime now);
}
