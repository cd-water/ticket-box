package com.cdwater.ticketbox.order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CreateMovieOrderRequest {

    @NotNull
    private Long screeningId;

    @NotEmpty
    @Size(max = 6)
    private List<Long> seatIds;
}
