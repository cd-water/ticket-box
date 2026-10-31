package com.cdwater.ticketbox.user.adapter.out.security;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.cdwater.ticketbox.config.SaTokenSetup;
import com.cdwater.ticketbox.user.application.port.UserAuthSession.LoginToken;
import com.cdwater.ticketbox.user.application.port.UserAuthSession;
import org.springframework.stereotype.Component;

@Component
public class SaTokenUserAuthAdapter implements UserAuthSession {

    @Override
    public LoginToken login(Long userId) {
        SaTokenSetup.USER.login(userId);
        SaTokenInfo info = SaTokenSetup.USER.getTokenInfo();
        return new LoginToken(info.tokenName, info.tokenValue);
    }

    @Override
    public void logout() {
        SaTokenSetup.USER.logout();
    }
}
