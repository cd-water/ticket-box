package com.cdwater.movietimecinema.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CinemaDetailVO {

    private Long id;

    private String name;

    private String address;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private String phone;

    private String tags;

    private List<MovieInCinemaVO> movies;
}
