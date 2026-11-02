package com.cdwater.ticketbox.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePasswordCommand {

    @NotBlank
    @Size(min = 6, max = 20)
    private String password;
}
