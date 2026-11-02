package com.cdwater.ticketbox.identity.application;

import com.cdwater.ticketbox.admin.domain.model.Admin;
import com.cdwater.ticketbox.admin.domain.repository.AdminRepository;
import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminIdentityService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public Admin verifyPassword(String username, String password) {
        Admin admin = adminRepository.findByUsername(username).orElse(null);
        if (admin == null || !passwordEncoder.matches(password, admin.getPassword())) {
            throw new BizException(ResultCode.BAD_REQUEST, "账号或密码错误");
        }
        if (!admin.isEnabled()) {
            throw new BizException(ResultCode.FORBIDDEN, "账号已禁用");
        }
        return admin;
    }
}
