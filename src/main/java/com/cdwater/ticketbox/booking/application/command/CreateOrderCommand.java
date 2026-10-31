package com.cdwater.ticketbox.booking.application.command;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderCommand {

    @NotNull
    private Long screeningId;

    @NotEmpty
    @Size(max = 6)
    private List<Long> seatIds;
}
