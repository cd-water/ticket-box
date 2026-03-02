package com.cdwater.movietimeuser.model.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AppFpwdDTO {

    @Pattern(regexp = "^1[3-9]\\d{9}$")
    private String phone;

    @Pattern(regexp = "^\\d{6}$")
    private String code;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,20}$")
    private String newPassword;
}
