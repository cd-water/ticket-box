package com.cdwater.movietimecinema.model.dto;

import com.cdwater.movietimecommon.PageRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ScreeningPageDTO extends PageRequest {

    private String movieName;

    @NotNull
    private Long cinemaId;
}
