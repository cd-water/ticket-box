package com.cdwater.ticketbox.cinema.adapter.out.persistence.projection;

import lombok.Data;

@Data
public class SeatInventoryCountRow {

    private Long screeningId;
    private Long availableCount;
}
