package com.cdwater.ticketbox.order.api.dto;

import java.io.Serializable;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShowOrderListQueryDTO  implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Min(0)
    @Max(2)
    private Integer status = 0;

    @Min(1)
    private long page = 1;

    @Min(1)
    @Max(30)
    private long size = 10;
}
