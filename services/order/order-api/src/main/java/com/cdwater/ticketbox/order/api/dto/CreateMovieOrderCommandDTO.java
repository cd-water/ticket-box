package com.cdwater.ticketbox.order.api.dto;

import java.io.Serializable;

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
public class CreateMovieOrderCommandDTO  implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private Long screeningId;

    @NotEmpty
    @Size(max = 6)
    private List<Long> seatIds;
}
