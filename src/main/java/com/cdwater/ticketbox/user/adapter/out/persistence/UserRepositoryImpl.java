package com.cdwater.ticketbox.user.adapter.out.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cdwater.ticketbox.user.domain.model.User;
import com.cdwater.ticketbox.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserMapper userMapper;

    @Override
    public Optional<User> findByPhone(String phone) {
        return Optional.ofNullable(userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getPhone, phone)));
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(userMapper.selectById(id));
    }

    @Override
    public List<User> findByIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return userMapper.selectByIds(ids);
    }

    @Override
    public User save(User user) {
        userMapper.insert(user);
        return user;
    }

    @Override
    public User findOrCreateByPhone(String phone, Supplier<User> newUser) {
        Optional<User> existing = findByPhone(phone);
        if (existing.isPresent()) {
            return existing.get();
        }
        try {
            return save(newUser.get());
        } catch (DuplicateKeyException e) {
            return findByPhone(phone)
                    .orElseThrow(() -> new IllegalStateException("用户创建失败", e));
        }
    }
}
