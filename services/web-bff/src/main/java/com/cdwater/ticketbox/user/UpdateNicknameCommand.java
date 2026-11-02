package com.cdwater.ticketbox.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateNicknameCommand {

    @NotBlank
    private String nickname;
}
