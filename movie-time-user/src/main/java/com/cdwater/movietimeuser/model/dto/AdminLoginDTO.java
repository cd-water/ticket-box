package com.cdwater.movietimeuser.model.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AdminLoginDTO {

    @Pattern(regexp = "^.{5,20}$")
    private String username;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,20}$")
    private String password;
}