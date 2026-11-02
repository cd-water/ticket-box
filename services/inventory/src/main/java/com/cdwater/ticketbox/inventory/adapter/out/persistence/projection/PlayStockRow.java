package com.cdwater.ticketbox.inventory.adapter.out.persistence.projection;

import lombok.Data;

@Data
public class PlayStockRow {

    private Long itemId;
    private Integer stock;
}
