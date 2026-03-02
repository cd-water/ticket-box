package com.cdwater.movietimecinema.model.vo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CinemaQueryVO {

    private Long id;

    private String name;

    private String address;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private String phone;

    private String tags;
}
