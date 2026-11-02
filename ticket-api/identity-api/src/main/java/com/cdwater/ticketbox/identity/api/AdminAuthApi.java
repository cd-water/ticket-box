package com.cdwater.ticketbox.identity.api;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.identity.api.dto.AdminBriefDTO;

public interface AdminAuthApi {

    AdminBriefDTO verifyPassword(String username, String password) throws BizException;
}
