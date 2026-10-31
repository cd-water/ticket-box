package com.cdwater.ticketbox.booking.application.port;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservedSeat {

    private Long seatId;
    private Integer rowNum;
    private Integer colNum;
}
