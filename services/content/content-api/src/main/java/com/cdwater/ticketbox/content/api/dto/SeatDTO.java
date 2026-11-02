package com.cdwater.ticketbox.content.api.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SeatDTO  implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long seatId;
    private Integer rowNum;
    private Integer colNum;
    private Integer status;
}
