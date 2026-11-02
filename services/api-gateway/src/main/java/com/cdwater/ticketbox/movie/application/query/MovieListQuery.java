package com.cdwater.ticketbox.movie.application.query;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MovieListQuery {

    @Pattern(regexp = "showing|upcoming")
    private String tab = "showing";

    @Size(max = 64)
    private String keyword = "";

    @Min(1)
    private long page = 1;

    @Min(1)
    @Max(30)
    private long size = 12;
}
