package com.cdwater.ticketbox.content.movie.application.port;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface UserInfoPort {

    Map<Long, UserInfo> findByIds(Collection<Long> ids);

    Optional<UserInfo> findById(Long id);
}
