package com.cdwater.movietimecinema.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieInCinemaVO {

    private Long id;

    private String name;

    private String cover;

    private String type;

    private Integer duration;

    private Double rating;
}
