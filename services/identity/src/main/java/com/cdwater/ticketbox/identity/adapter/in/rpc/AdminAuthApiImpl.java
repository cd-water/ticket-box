package com.cdwater.ticketbox.identity.adapter.in.rpc;

import com.cdwater.ticketbox.admin.domain.model.Admin;
import com.cdwater.ticketbox.identity.api.AdminAuthApi;
import com.cdwater.ticketbox.identity.api.dto.AdminBriefDTO;
import com.cdwater.ticketbox.identity.application.AdminIdentityService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
@RequiredArgsConstructor
public class AdminAuthApiImpl implements AdminAuthApi {

    private final AdminIdentityService adminIdentityService;

    @Override
    public AdminBriefDTO verifyPassword(String username, String password) {
        Admin admin = adminIdentityService.verifyPassword(username, password);
        return new AdminBriefDTO(admin.getId(), admin.getUsername());
    }
}
