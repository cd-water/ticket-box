package com.cdwater.ticketbox.inventory.adapter.out.persistence.mapper;

import com.cdwater.ticketbox.inventory.adapter.out.persistence.po.ShowSeatPO;
import com.cdwater.ticketbox.inventory.adapter.out.persistence.projection.ZoneAvailabilityRow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Mapper
public interface ShowSeatMapper {

    @Select("""
            SELECT zone, COUNT(*) AS available
            FROM t_show_seat
            WHERE show_id = #{showId}
              AND (status = 0 OR (status = 1 AND lock_expire_time < NOW()))
            GROUP BY zone
            """)
    List<ZoneAvailabilityRow> countAvailableByZone(@Param("showId") Long showId);

    @Select("""
            SELECT id, show_id, zone, seat_no, status
            FROM t_show_seat
            WHERE show_id = #{showId} AND zone = #{zone}
              AND (status = 0 OR (status = 1 AND lock_expire_time < #{now}))
            ORDER BY seat_no
            FOR UPDATE
            """)
    List<ShowSeatPO> lockAvailableSeats(@Param("showId") Long showId,
                                        @Param("zone") String zone,
                                        @Param("now") LocalDateTime now);

    @Update("""
            <script>
            UPDATE t_show_seat
            SET status = 1, lock_order_no = #{orderNo}, lock_expire_time = #{expireTime}
            WHERE id IN
            <foreach collection="seatIds" item="id" open="(" separator="," close=")">#{id}</foreach>
            </script>
            """)
    int lockSeats(@Param("seatIds") Collection<Long> seatIds,
                  @Param("orderNo") Long orderNo,
                  @Param("expireTime") LocalDateTime expireTime);

    @Select("""
            SELECT id, show_id, zone, seat_no, status
            FROM t_show_seat
            WHERE lock_order_no = #{orderNo} AND status IN (1, 2)
            ORDER BY seat_no
            """)
    List<ShowSeatPO> selectLockedByOrder(@Param("orderNo") Long orderNo);

    @Update("""
            UPDATE t_show_seat SET status = 2, lock_expire_time = NULL
            WHERE lock_order_no = #{orderNo} AND status = 1
            """)
    int confirmByOrder(@Param("orderNo") Long orderNo);

    @Update("""
            UPDATE t_show_seat SET status = 0, lock_order_no = NULL, lock_expire_time = NULL
            WHERE lock_order_no = #{orderNo} AND status IN (1, 2)
            """)
    int releaseByOrder(@Param("orderNo") Long orderNo);
}
