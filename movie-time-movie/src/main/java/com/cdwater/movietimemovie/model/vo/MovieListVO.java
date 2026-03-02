package com.cdwater.movietimemovie.model.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovieListVO {

    private Long id;

    private String name;

    private String cover;

    private String director;

    private String actors;

    private Double rating;
}
