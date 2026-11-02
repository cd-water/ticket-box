package com.cdwater.ticketbox.order.api.dto;

import java.io.Serializable;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreatePlayOrderCommandDTO  implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Min(1)
    private Long itemId;

    @NotNull
    @Min(1)
    @Max(10)
    private Integer quantity;
}
