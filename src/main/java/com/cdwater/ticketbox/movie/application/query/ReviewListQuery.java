package com.cdwater.ticketbox.movie.application.query;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ReviewListQuery {

    @Pattern(regexp = "hot|latest")
    private String sort = "hot";

    private String cursor = "";

    @Min(1)
    @Max(20)
    private long size = 10;
}
