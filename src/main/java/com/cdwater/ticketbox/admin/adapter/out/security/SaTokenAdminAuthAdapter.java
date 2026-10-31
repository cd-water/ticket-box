package com.cdwater.ticketbox.admin.adapter.out.security;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.cdwater.ticketbox.admin.application.port.AdminAuthSession;
import com.cdwater.ticketbox.admin.application.port.AdminAuthSession.LoginToken;
import com.cdwater.ticketbox.config.SaTokenSetup;
import org.springframework.stereotype.Component;

@Component
public class SaTokenAdminAuthAdapter implements AdminAuthSession {

    @Override
    public LoginToken login(Long adminId) {
        SaTokenSetup.ADMIN.login(adminId);
        SaTokenInfo info = SaTokenSetup.ADMIN.getTokenInfo();
        return new LoginToken(info.tokenName, info.tokenValue);
    }

    @Override
    public void logout() {
        SaTokenSetup.ADMIN.logout();
    }
}
