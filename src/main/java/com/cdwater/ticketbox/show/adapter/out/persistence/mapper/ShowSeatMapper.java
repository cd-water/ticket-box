package com.cdwater.ticketbox.show.adapter.out.persistence.mapper;

import com.cdwater.ticketbox.show.adapter.out.persistence.projection.ZoneAvailabilityRow;
import com.cdwater.ticketbox.show.adapter.out.persistence.po.ShowSeatPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Collection;
import java.util.List;

@Mapper
public interface ShowSeatMapper {

    @Select("""
            SELECT zone, COUNT(*) AS available
            FROM t_show_seat
            WHERE show_id = #{showId} AND status = 0
            GROUP BY zone
            """)
    List<ZoneAvailabilityRow> countAvailableByZone(@Param("showId") Long showId);

    @Select("""
            SELECT id, show_id, zone, seat_no, status
            FROM t_show_seat
            WHERE show_id = #{showId} AND zone = #{zone} AND status = 0
            ORDER BY seat_no
            FOR UPDATE
            """)
    List<ShowSeatPO> lockAvailableSeats(@Param("showId") Long showId, @Param("zone") String zone);

    @Update("""
            <script>
            UPDATE t_show_seat
            SET status = 2, lock_order_no = #{orderNo}, lock_expire_time = NULL
            WHERE id IN
            <foreach collection="seatIds" item="id" open="(" separator="," close=")">#{id}</foreach>
            </script>
            """)
    int markSold(@Param("seatIds") Collection<Long> seatIds, @Param("orderNo") Long orderNo);
}
