package com.cdwater.movietimemovie.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MovieUpdateDTO {

    @NotNull
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String cover;

    @NotBlank
    private String type;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @NotNull
    private Integer duration;

    @NotBlank
    private String description;

    @NotBlank
    private String director;

    @NotBlank
    private String actors;

    @NotNull
    @Min(value = 0)
    @Max(value = 2)
    private Integer status;
}
