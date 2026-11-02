package com.cdwater.ticketbox.admin.application.port;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface AdminAuthSession {

    LoginToken login(Long adminId);

    void logout();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class LoginToken {

        private String tokenName;
        private String tokenValue;
    }
}
