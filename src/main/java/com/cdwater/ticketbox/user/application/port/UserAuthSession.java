package com.cdwater.ticketbox.user.application.port;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface UserAuthSession {

    LoginToken login(Long userId);

    void logout();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class LoginToken {

        private String tokenName;
        private String tokenValue;
    }
}
