package com.cdwater.ticketbox.user.application.query;

import com.cdwater.ticketbox.identity.api.UserAuthApi;
import com.cdwater.ticketbox.identity.api.UserQueryApi;
import com.cdwater.ticketbox.identity.api.dto.UserBriefDTO;
import com.cdwater.ticketbox.identity.api.dto.UserProfileDTO;
import com.cdwater.ticketbox.user.application.query.view.ProfileView;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserQueryService {

    @DubboReference(check = false, timeout = 5000)
    private UserQueryApi userQueryApi;

    @DubboReference(check = false, timeout = 5000)
    private UserAuthApi userAuthApi;

    public Map<Long, UserBrief> findByIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Map.of();
        }
        return userQueryApi.findByIds(ids).stream()
                .collect(Collectors.toMap(UserBriefDTO::getId, this::toBrief));
    }

    public Optional<UserBrief> findById(Long id) {
        return Optional.ofNullable(userQueryApi.findById(id)).map(this::toBrief);
    }

    public ProfileView profile(Long userId) {
        return toProfile(userAuthApi.getProfile(userId));
    }

    private UserBrief toBrief(UserBriefDTO dto) {
        return new UserBrief(dto.getId(), dto.getNickname(), dto.getAvatar());
    }

    private ProfileView toProfile(UserProfileDTO dto) {
        return new ProfileView(dto.getId(), dto.getPhone(), dto.getNickname(), dto.getAvatar());
    }
}
