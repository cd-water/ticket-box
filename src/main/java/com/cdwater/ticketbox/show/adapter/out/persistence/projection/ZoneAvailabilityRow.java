package com.cdwater.ticketbox.show.adapter.out.persistence.projection;

import lombok.Data;

@Data
public class ZoneAvailabilityRow {

    private String zone;
    private Long available;
}
