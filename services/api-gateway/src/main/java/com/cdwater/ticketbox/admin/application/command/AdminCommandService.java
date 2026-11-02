package com.cdwater.ticketbox.admin.application.command;

import com.cdwater.ticketbox.admin.application.port.AdminAuthSession;
import com.cdwater.ticketbox.admin.application.port.AdminAuthSession.LoginToken;
import com.cdwater.ticketbox.admin.application.query.view.LoginView;
import com.cdwater.ticketbox.admin.domain.model.Admin;
import com.cdwater.ticketbox.admin.domain.repository.AdminRepository;
import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/** 管理员认证命令：登录与注销。 */
@Service
@RequiredArgsConstructor
public class AdminCommandService {

    private final AdminRepository adminRepository;
    private final AdminAuthSession adminAuthSession;
    private final PasswordEncoder passwordEncoder;

    /** 账号或密码错误统一提示，避免泄露账号信息。 */
    public LoginView login(AdminLoginCommand command) {
        Admin admin = adminRepository.findByUsername(command.getUsername()).orElse(null);
        if (admin == null || !passwordEncoder.matches(command.getPassword(), admin.getPassword())) {
            throw new BizException(ResultCode.BAD_REQUEST, "账号或密码错误");
        }
        return completeLogin(admin);
    }

    public void logout() {
        adminAuthSession.logout();
    }

    private LoginView completeLogin(Admin admin) {
        if (!admin.isEnabled()) {
            throw new BizException(ResultCode.FORBIDDEN, "账号已禁用");
        }
        LoginToken token = adminAuthSession.login(admin.getId());
        return new LoginView(token.getTokenName(), token.getTokenValue(),
                admin.getId(), admin.getUsername());
    }
}
