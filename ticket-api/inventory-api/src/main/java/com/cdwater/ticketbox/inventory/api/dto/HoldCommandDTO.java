package com.cdwater.ticketbox.inventory.api.dto;

import com.cdwater.ticketbox.common.ticket.ProductType;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class HoldCommandDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private ProductType productType;
    /** 电影=排场ID，演出=演出ID，玩乐=项目ID */
    private Long productId;
    /** 演出区域；电影/玩乐为空 */
    private String zone;
    /** 电影选座；演出/玩乐为空 */
    private List<Long> seatIds;
    /** 演出/玩乐数量；电影取 seatIds.size() */
    private Integer quantity;
    private Long orderNo;
    private LocalDateTime expireTime;
}
