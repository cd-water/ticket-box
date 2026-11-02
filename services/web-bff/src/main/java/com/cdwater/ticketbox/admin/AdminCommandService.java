package com.cdwater.ticketbox.admin;

import com.cdwater.ticketbox.admin.AdminAuthSession;
import com.cdwater.ticketbox.admin.AdminAuthSession.LoginToken;
import com.cdwater.ticketbox.admin.LoginView;
import com.cdwater.ticketbox.identity.api.AdminAuthApi;
import com.cdwater.ticketbox.identity.api.dto.AdminBriefDTO;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminCommandService {

    @DubboReference(check = false, timeout = 5000)
    private AdminAuthApi adminAuthApi;

    private final AdminAuthSession adminAuthSession;

    public LoginView login(AdminLoginCommand command) {
        AdminBriefDTO admin = adminAuthApi.verifyPassword(
                command.getUsername(), command.getPassword());
        LoginToken token = adminAuthSession.login(admin.getId());
        return new LoginView(token.getTokenName(), token.getTokenValue(),
                admin.getId(), admin.getUsername());
    }

    public void logout() {
        adminAuthSession.logout();
    }
}
