package com.cdwater.movietimeuser.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserEditDTO {

    @Size(min = 2, max = 15)
    private String nickname;

    private String avatar;

    @Min(value = 1)
    @Max(value = 2)
    private Integer gender;

    @PastOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Size(max = 100)
    private String introduce;
}
