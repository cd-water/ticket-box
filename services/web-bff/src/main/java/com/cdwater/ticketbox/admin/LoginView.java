package com.cdwater.ticketbox.admin;

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
    private String username;
}
