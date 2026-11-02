package com.cdwater.ticketbox.play.adapter.out.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdwater.ticketbox.play.adapter.out.persistence.po.PlayItemPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface PlayItemMapper extends BaseMapper<PlayItemPO> {

    @Update("""
            UPDATE t_play_item SET stock = stock - #{quantity}
            WHERE id = #{itemId} AND status = 1 AND stock >= #{quantity}
            """)
    int deductStock(@Param("itemId") Long itemId, @Param("quantity") int quantity);

    @Update("UPDATE t_play_item SET stock = stock + #{quantity} WHERE id = #{itemId}")
    int restoreStock(@Param("itemId") Long itemId, @Param("quantity") int quantity);
}
