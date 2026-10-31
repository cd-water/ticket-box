package com.cdwater.ticketbox.cinema.infrastructure.persistence.projection;

import lombok.Data;

@Data
public class ScreeningSeatCountRow {

    private Long screeningId;
    private Long availableCount;
}
