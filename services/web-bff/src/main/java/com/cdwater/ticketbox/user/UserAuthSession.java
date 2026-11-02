package com.cdwater.ticketbox.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface UserAuthSession {

    LoginToken login(Long userId);

    void logout();

    void logoutAll(Long userId);

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class LoginToken {

        private String tokenName;
        private String tokenValue;
    }
}
