package com.cdwater.ticketbox.admin.application.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminLoginCommand {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
