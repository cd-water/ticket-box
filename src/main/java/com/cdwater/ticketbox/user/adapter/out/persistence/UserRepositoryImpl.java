package com.cdwater.ticketbox.user.adapter.out.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cdwater.ticketbox.user.adapter.out.persistence.converter.UserConverter;
import com.cdwater.ticketbox.user.adapter.out.persistence.mapper.UserMapper;
import com.cdwater.ticketbox.user.adapter.out.persistence.po.UserPO;
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
    private final UserConverter userConverter;

    @Override
    public Optional<User> findByPhone(String phone) {
        return Optional.ofNullable(userMapper.selectOne(
                        new LambdaQueryWrapper<UserPO>().eq(UserPO::getPhone, phone)))
                .map(userConverter::toDomain);
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(userMapper.selectById(id))
                .map(userConverter::toDomain);
    }

    @Override
    public List<User> findByIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return userConverter.toDomain(userMapper.selectByIds(ids));
    }

    @Override
    public User save(User user) {
        UserPO po = userConverter.toPO(user);
        userMapper.insert(po);
        user.setId(po.getId());
        return user;
    }

    @Override
    public boolean updateNickname(Long id, String nickname) {
        return userMapper.update(null, new LambdaUpdateWrapper<UserPO>()
                .eq(UserPO::getId, id)
                .set(UserPO::getNickname, nickname)) == 1;
    }

    @Override
    public boolean updateAvatar(Long id, String avatar) {
        return userMapper.update(null, new LambdaUpdateWrapper<UserPO>()
                .eq(UserPO::getId, id)
                .set(UserPO::getAvatar, avatar)) == 1;
    }

    @Override
    public boolean updatePassword(Long id, String passwordHash) {
        return userMapper.update(null, new LambdaUpdateWrapper<UserPO>()
                .eq(UserPO::getId, id)
                .set(UserPO::getPassword, passwordHash)) == 1;
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
