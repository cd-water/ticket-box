package com.cdwater.ticketbox.user.application.command;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateNicknameCommand {

    @NotBlank
    private String nickname;
}
