package com.cdwater.ticketbox.admin.adapter.out.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cdwater.ticketbox.admin.adapter.out.persistence.converter.AdminConverter;
import com.cdwater.ticketbox.admin.adapter.out.persistence.mapper.AdminMapper;
import com.cdwater.ticketbox.admin.adapter.out.persistence.po.AdminPO;
import com.cdwater.ticketbox.admin.domain.model.Admin;
import com.cdwater.ticketbox.admin.domain.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AdminRepositoryImpl implements AdminRepository {

    private final AdminMapper adminMapper;
    private final AdminConverter adminConverter;

    @Override
    public Optional<Admin> findByUsername(String username) {
        return Optional.ofNullable(adminConverter.toDomain(adminMapper.selectOne(
                new LambdaQueryWrapper<AdminPO>().eq(AdminPO::getUsername, username))));
    }

    @Override
    public Optional<Admin> findById(Long id) {
        return Optional.ofNullable(adminMapper.selectById(id)).map(adminConverter::toDomain);
    }
}
