package com.cdwater.ticketbox.inventory.adapter.out.persistence.mapper;

import com.cdwater.ticketbox.inventory.adapter.out.persistence.projection.PlayStockRow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Collection;
import java.util.List;

@Mapper
public interface PlayStockMapper {

    @Update("""
            UPDATE t_play_stock SET stock = stock - #{quantity}
            WHERE item_id = #{itemId} AND stock >= #{quantity}
            """)
    int deduct(@Param("itemId") Long itemId, @Param("quantity") int quantity);

    @Update("""
            UPDATE t_play_stock SET stock = stock + #{quantity}
            WHERE item_id = #{itemId}
            """)
    int restore(@Param("itemId") Long itemId, @Param("quantity") int quantity);

    @Select("""
            <script>
            SELECT item_id, stock FROM t_play_stock
            WHERE item_id IN
            <foreach collection="itemIds" item="id" open="(" separator="," close=")">#{id}</foreach>
            </script>
            """)
    List<PlayStockRow> selectStocks(@Param("itemIds") Collection<Long> itemIds);
}
