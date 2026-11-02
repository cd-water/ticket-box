package com.cdwater.ticketbox.show.application.command;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateShowOrderCommand {

    @NotNull
    @Min(1)
    private Long showId;

    @NotNull
    @Min(1)
    private Long ticketTypeId;

    @NotNull
    @Min(1)
    @Max(6)
    private Integer quantity;
}
