package com.cdwater.ticketbox.auth.application.query;

import com.cdwater.ticketbox.auth.domain.model.User;
import com.cdwater.ticketbox.auth.infrastructure.persistence.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final UserMapper userMapper;

    public Map<Long, UserBrief> findByIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Map.of();
        }
        return userMapper.selectByIds(ids).stream()
                .collect(Collectors.toMap(User::getId, this::toBrief));
    }

    public Optional<UserBrief> findById(Long id) {
        return Optional.ofNullable(userMapper.selectById(id)).map(this::toBrief);
    }

    private UserBrief toBrief(User user) {
        return new UserBrief(user.getId(), user.getNickname(), user.getAvatar());
    }
}
