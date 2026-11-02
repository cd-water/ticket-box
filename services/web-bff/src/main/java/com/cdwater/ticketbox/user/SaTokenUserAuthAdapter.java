package com.cdwater.ticketbox.user;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.cdwater.ticketbox.config.SaTokenSetup;
import com.cdwater.ticketbox.user.UserAuthSession.LoginToken;
import com.cdwater.ticketbox.user.UserAuthSession;
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

    @Override
    public void logoutAll(Long userId) {
        SaTokenSetup.USER.logout(userId);
    }
}
