package com.cdwater.ticketbox.inventory.api.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShowSeatSnapshotDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String zone;
    private Integer seatNo;
}
