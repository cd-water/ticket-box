package com.cdwater.ticketbox.order;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreatePlayOrderRequest {

    @NotNull
    @Min(1)
    private Long itemId;

    @NotNull
    @Min(1)
    @Max(10)
    private Integer quantity;
}
