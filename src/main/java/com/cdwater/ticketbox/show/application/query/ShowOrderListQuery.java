package com.cdwater.ticketbox.show.application.query;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShowOrderListQuery {

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
