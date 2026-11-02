package com.cdwater.ticketbox.identity.api;

import com.cdwater.ticketbox.identity.api.dto.AdminBriefDTO;

public interface AdminAuthApi {

    AdminBriefDTO verifyPassword(String username, String password);
}
