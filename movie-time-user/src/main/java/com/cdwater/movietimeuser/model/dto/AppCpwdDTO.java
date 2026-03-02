package com.cdwater.movietimeuser.model.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AppCpwdDTO {

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,20}$")
    private String newPassword;
}
