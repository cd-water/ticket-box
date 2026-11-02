package com.cdwater.ticketbox.inventory.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryUnitDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long seatId;
    private String zone;
    private Integer seatNo;
    private Integer rowNum;
    private Integer colNum;
}
