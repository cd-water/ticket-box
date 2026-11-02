package com.cdwater.ticketbox.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginView {

    private String tokenName;
    private String tokenValue;
    private Long id;
    private String phone;
    private String nickname;
    private String avatar;
}
