package com.cdwater.ticketbox.inventory.api.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MovieSeatSnapshotDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Integer rowNum;
    private Integer colNum;
    private Integer status;
}
