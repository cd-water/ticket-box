package com.cdwater.ticketbox.inventory.domain.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShowSeat {

    private Long id;
    private Long showId;
    private String zone;
    private Integer seatNo;
    private Integer status;
    private Long lockOrderNo;
    private LocalDateTime lockExpireTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
