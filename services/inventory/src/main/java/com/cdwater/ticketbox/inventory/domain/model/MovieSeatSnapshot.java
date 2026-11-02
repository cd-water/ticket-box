package com.cdwater.ticketbox.inventory.domain.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MovieSeatSnapshot {

    private Long seatId;
    private Integer rowNum;
    private Integer colNum;
    private Integer status;
    private LocalDateTime lockExpireTime;
}
