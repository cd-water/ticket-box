package com.cdwater.movietimecinema.model.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CinemaListVO {

    private Long id;

    private String name;

    private String address;

    private String tags;

    private Double distance;
}
