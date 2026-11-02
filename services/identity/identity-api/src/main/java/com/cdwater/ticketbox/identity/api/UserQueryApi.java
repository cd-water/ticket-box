package com.cdwater.ticketbox.identity.api;

import com.cdwater.ticketbox.identity.api.dto.UserBriefDTO;

import java.util.Collection;
import java.util.List;

public interface UserQueryApi {

    UserBriefDTO findById(long id);

    List<UserBriefDTO> findByIds(Collection<Long> ids);
}
