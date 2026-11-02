package com.cdwater.ticketbox.content.movie.adapter.out.rpc;

import com.cdwater.ticketbox.content.movie.application.port.UserInfo;
import com.cdwater.ticketbox.content.movie.application.port.UserInfoPort;
import com.cdwater.ticketbox.identity.api.UserQueryApi;
import com.cdwater.ticketbox.identity.api.dto.UserBriefDTO;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReviewUserAdapter implements UserInfoPort {

    @DubboReference(check = false, timeout = 3000)
    private UserQueryApi userQueryApi;

    @Override
    public Map<Long, UserInfo> findByIds(Collection<Long> ids) {
        return userQueryApi.findByIds(ids).stream()
                .collect(Collectors.toMap(UserBriefDTO::getId, this::toUserInfo));
    }

    @Override
    public Optional<UserInfo> findById(Long id) {
        UserBriefDTO dto = userQueryApi.findById(id);
        return Optional.ofNullable(dto).map(this::toUserInfo);
    }

    private UserInfo toUserInfo(UserBriefDTO dto) {
        return new UserInfo(dto.getId(), dto.getNickname(), dto.getAvatar());
    }
}
