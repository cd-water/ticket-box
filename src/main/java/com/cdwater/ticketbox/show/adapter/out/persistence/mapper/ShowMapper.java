package com.cdwater.ticketbox.show.adapter.out.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdwater.ticketbox.show.adapter.out.persistence.po.ShowPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShowMapper extends BaseMapper<ShowPO> {
}
