package com.cdwater.ticketbox.content.api.dto;

import java.io.Serializable;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ReplyListQueryDTO  implements Serializable {

    private static final long serialVersionUID = 1L;

    private String cursor = "";

    @Min(1)
    @Max(20)
    private long size = 10;
}
