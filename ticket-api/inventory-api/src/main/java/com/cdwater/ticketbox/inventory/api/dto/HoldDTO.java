package com.cdwater.ticketbox.inventory.api.dto;

import com.cdwater.ticketbox.common.ticket.ProductType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class HoldDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private long orderNo;
    private ProductType productType;
    /** 电影/演出为锁定的座位，玩乐为空 */
    private List<InventoryUnitDTO> units;
}
