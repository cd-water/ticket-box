package com.cdwater.ticketbox.user.application.query;

import com.cdwater.ticketbox.shared.error.BizException;
import com.cdwater.ticketbox.shared.error.ResultCode;
import com.cdwater.ticketbox.user.application.query.converter.UserBriefConverter;
import com.cdwater.ticketbox.user.application.query.converter.UserViewConverter;
import com.cdwater.ticketbox.user.application.query.view.ProfileView;
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
    private final UserBriefConverter userBriefConverter;
    private final UserViewConverter userViewConverter;

    public Map<Long, UserBrief> findByIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Map.of();
        }
        return userRepository.findByIds(ids).stream()
                .collect(Collectors.toMap(User::getId, userBriefConverter::toBrief));
    }

    public Optional<UserBrief> findById(Long id) {
        return userRepository.findById(id).map(userBriefConverter::toBrief);
    }

    public ProfileView profile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "用户不存在"));
        return userViewConverter.toProfileView(user);
    }
}
