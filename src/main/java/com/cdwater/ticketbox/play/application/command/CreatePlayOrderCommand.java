package com.cdwater.ticketbox.play.application.command;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreatePlayOrderCommand {

    @NotNull
    @Min(1)
    private Long itemId;

    @NotNull
    @Min(1)
    @Max(10)
    private Integer quantity;
}
