package com.cdwater.ticketbox.cinema.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SeatVO {

    private Long seatId;
    private Integer rowNum;
    private Integer colNum;
    private Integer status;
}
