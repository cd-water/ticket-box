package com.cdwater.ticketbox.identity.adapter.in.rpc;

import com.cdwater.ticketbox.identity.api.UserQueryApi;
import com.cdwater.ticketbox.identity.api.dto.UserBriefDTO;
import com.cdwater.ticketbox.identity.application.UserIdentityService;
import com.cdwater.ticketbox.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.Collection;
import java.util.List;

@DubboService
@RequiredArgsConstructor
public class UserQueryApiImpl implements UserQueryApi {

    private final UserIdentityService userIdentityService;

    @Override
    public UserBriefDTO findById(long id) {
        return userIdentityService.findById(id).map(this::toBrief).orElse(null);
    }

    @Override
    public List<UserBriefDTO> findByIds(Collection<Long> ids) {
        return userIdentityService.findByIds(ids).stream().map(this::toBrief).toList();
    }

    private UserBriefDTO toBrief(User user) {
        return new UserBriefDTO(user.getId(), user.getNickname(), user.getAvatar());
    }
}
