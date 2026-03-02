package com.cdwater.movietimemovie.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewAddDTO {

    @NotNull
    private Long movieId;

    @NotBlank
    private String content;

    @NotNull
    private Long parentId;
}