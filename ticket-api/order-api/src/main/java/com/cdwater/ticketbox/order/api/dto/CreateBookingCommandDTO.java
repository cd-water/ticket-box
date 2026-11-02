package com.cdwater.ticketbox.order.api.dto;

import com.cdwater.ticketbox.common.ticket.ProductType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CreateBookingCommandDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private ProductType productType;
    /** 电影=排场ID，演出=演出ID，玩乐=项目ID */
    private Long productId;
    /** 演出票档ID；电影/玩乐为空 */
    private Long ticketTypeId;
    /** 电影选座；演出/玩乐为空 */
    private List<Long> seatIds;
    /** 演出/玩乐数量；电影取 seatIds.size() */
    private Integer quantity;
}
