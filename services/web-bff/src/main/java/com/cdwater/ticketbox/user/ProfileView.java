package com.cdwater.ticketbox.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileView {

    private Long id;
    private String phone;
    private String nickname;
    private String avatar;
}
