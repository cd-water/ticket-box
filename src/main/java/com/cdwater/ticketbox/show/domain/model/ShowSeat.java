package com.cdwater.ticketbox.show.domain.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShowSeat {

    public static final int STATUS_AVAILABLE = 0;
    public static final int STATUS_SOLD = 2;

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
