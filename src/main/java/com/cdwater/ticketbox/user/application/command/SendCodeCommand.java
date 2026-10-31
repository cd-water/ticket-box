package com.cdwater.ticketbox.user.application.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendCodeCommand {

    @NotBlank
    @Pattern(regexp = "^1[3-9]\\d{9}$")
    private String phone;
}
