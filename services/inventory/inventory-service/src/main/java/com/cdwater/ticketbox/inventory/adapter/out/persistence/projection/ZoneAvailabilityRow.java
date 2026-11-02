package com.cdwater.ticketbox.inventory.adapter.out.persistence.projection;

import lombok.Data;

@Data
public class ZoneAvailabilityRow {

    private String zone;
    private Long available;
}
