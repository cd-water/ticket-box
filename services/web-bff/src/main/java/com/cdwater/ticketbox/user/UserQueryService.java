package com.cdwater.ticketbox.user;

import com.cdwater.ticketbox.identity.api.UserAuthApi;
import com.cdwater.ticketbox.identity.api.dto.UserProfileDTO;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserQueryService {

    @DubboReference(check = false, timeout = 5000)
    private UserAuthApi userAuthApi;

    public ProfileView getProfile(Long userId) {
        return toProfile(userAuthApi.getProfile(userId));
    }

    private ProfileView toProfile(UserProfileDTO dto) {
        return new ProfileView(dto.getId(), dto.getPhone(), dto.getNickname(), dto.getAvatar());
    }
}
