package com.cdwater.movietimemovie.model.dto;

import com.cdwater.movietimecommon.PageRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewPageDTO extends PageRequest {

    private String userNickname;

    @NotNull
    private Long movieId;

    private Integer status;
}
