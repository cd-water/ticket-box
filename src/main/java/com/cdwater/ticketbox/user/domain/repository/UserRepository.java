package com.cdwater.ticketbox.user.domain.repository;

import com.cdwater.ticketbox.user.domain.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public interface UserRepository {

    Optional<User> findByPhone(String phone);

    Optional<User> findById(Long id);

    List<User> findByIds(Collection<Long> ids);

    User save(User user);

    User findOrCreateByPhone(String phone, Supplier<User> newUser);
}
