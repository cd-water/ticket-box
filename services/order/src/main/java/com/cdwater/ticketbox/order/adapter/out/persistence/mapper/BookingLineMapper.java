package com.cdwater.ticketbox.order.adapter.out.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cdwater.ticketbox.order.adapter.out.persistence.po.BookingLinePO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookingLineMapper extends BaseMapper<BookingLinePO> {
}
