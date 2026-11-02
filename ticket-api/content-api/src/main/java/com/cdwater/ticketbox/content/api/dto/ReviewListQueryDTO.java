package com.cdwater.ticketbox.content.api.dto;

import java.io.Serializable;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ReviewListQueryDTO  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Pattern(regexp = "hot|latest")
    private String sort = "hot";

    private String cursor = "";

    @Min(1)
    @Max(20)
    private long size = 10;
}
