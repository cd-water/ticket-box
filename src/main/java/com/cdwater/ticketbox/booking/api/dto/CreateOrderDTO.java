package com.cdwater.ticketbox.booking.api.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderDTO {

    @NotNull
    private Long screeningId;

    @NotEmpty
    @Size(max = 6)
    private List<Long> seatIds;
}
