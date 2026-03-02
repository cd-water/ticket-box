package com.cdwater.movietimecinema.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ScreeningAddDTO {

    @NotNull
    private Long movieId;

    @NotNull
    private Long cinemaId;

    @NotBlank
    private String hallName;

    @NotNull
    @Positive
    private Integer rowCount;

    @NotNull
    @Positive
    private Integer colCount;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @NotNull
    @Positive
    private BigDecimal price;
}
