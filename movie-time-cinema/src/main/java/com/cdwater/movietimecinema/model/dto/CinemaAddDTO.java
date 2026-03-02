package com.cdwater.movietimecinema.model.dto;


import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CinemaAddDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String address;

    @NotNull
    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    private BigDecimal longitude;

    @NotNull
    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    private BigDecimal latitude;

    @NotBlank
    private String phone;

    @NotBlank
    private String tags;
}
