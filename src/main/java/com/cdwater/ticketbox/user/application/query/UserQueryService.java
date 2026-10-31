package com.cdwater.ticketbox.user.application.query;

import com.cdwater.ticketbox.user.application.query.mapper.UserBriefMapper;
import com.cdwater.ticketbox.user.domain.model.User;
import com.cdwater.ticketbox.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final UserRepository userRepository;
    private final UserBriefMapper userBriefMapper;

    public Map<Long, UserBrief> findByIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Map.of();
        }
        return userRepository.findByIds(ids).stream()
                .collect(Collectors.toMap(User::getId, userBriefMapper::toBrief));
    }

    public Optional<UserBrief> findById(Long id) {
        return userRepository.findById(id).map(userBriefMapper::toBrief);
    }
}
