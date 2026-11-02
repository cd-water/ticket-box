package com.cdwater.ticketbox.config;

import com.cdwater.ticketbox.shared.security.CurrentUser;
import org.springframework.stereotype.Component;

@Component
public class SaTokenCurrentUser implements CurrentUser {

    @Override
    public long id() {
        return SaTokenSetup.USER.getLoginIdAsLong();
    }
}
