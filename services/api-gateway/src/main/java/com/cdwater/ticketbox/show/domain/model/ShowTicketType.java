package com.cdwater.ticketbox.show.domain.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ShowTicketType {

    public static final int STATUS_ON_SALE = 1;

    private Long id;
    private Long showId;
    private String name;
    private String zone;
    private BigDecimal price;
    private Integer sortOrder;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public boolean isOnSale() {
        return Integer.valueOf(STATUS_ON_SALE).equals(status);
    }
}
