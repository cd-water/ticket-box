package com.cdwater.ticketbox.movie.adapter.out.client;

import com.cdwater.ticketbox.movie.application.port.UserInfo;
import com.cdwater.ticketbox.movie.application.port.UserInfoPort;
import com.cdwater.ticketbox.user.application.query.UserBrief;
import com.cdwater.ticketbox.user.application.query.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserInfoAdapter implements UserInfoPort {

    private final UserQueryService userQueryService;
    private final MovieUserInfoMapper movieUserInfoMapper;

    @Override
    public Map<Long, UserInfo> findByIds(Collection<Long> ids) {
        return userQueryService.findByIds(ids).entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> movieUserInfoMapper.toInfo(entry.getValue())));
    }

    @Override
    public Optional<UserInfo> findById(Long id) {
        return userQueryService.findById(id).map(movieUserInfoMapper::toInfo);
    }
}
