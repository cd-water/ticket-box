package com.cdwater.ticketbox.movie.application.query;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ReplyListQuery {

    private String cursor = "";

    @Min(1)
    @Max(20)
    private long size = 10;
}
