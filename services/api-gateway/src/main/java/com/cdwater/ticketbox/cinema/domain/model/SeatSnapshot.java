package com.cdwater.ticketbox.cinema.domain.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SeatSnapshot {

    private Long seatId;
    private Integer rowNum;
    private Integer colNum;
    private Integer status;
    private Long lockOrderNo;
    private LocalDateTime lockExpireTime;
}
