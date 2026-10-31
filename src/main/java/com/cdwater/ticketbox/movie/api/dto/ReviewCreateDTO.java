package com.cdwater.ticketbox.movie.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ReviewCreateDTO {

    @NotBlank
    @Size(max = 1000)
    private String content;
}
