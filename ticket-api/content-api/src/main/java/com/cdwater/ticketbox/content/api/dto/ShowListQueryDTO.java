package com.cdwater.ticketbox.content.api.dto;

import java.io.Serializable;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ShowListQueryDTO  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(max = 64)
    private String keyword = "";

    @Min(1)
    private long page = 1;

    @Min(1)
    @Max(30)
    private long size = 10;
}
