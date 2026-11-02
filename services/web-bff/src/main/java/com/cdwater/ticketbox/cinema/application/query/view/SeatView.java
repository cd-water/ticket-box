package com.cdwater.ticketbox.cinema.application.query.view;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SeatView {

    private Long seatId;
    private Integer rowNum;
    private Integer colNum;
    private Integer status;
}
