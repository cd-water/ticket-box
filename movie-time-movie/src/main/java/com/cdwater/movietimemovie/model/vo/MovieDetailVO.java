package com.cdwater.movietimemovie.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class MovieDetailVO {

    private Long id;

    private String name;

    private String cover;

    private String type;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    private Integer duration;

    private String description;

    private String director;

    private String actors;

    private Double rating;

    private Integer status;
}
