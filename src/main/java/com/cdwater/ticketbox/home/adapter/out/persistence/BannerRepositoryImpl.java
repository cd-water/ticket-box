package com.cdwater.ticketbox.home.adapter.out.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cdwater.ticketbox.home.adapter.out.persistence.mapper.BannerMapper;
import com.cdwater.ticketbox.home.adapter.out.persistence.converter.BannerConverter;
import com.cdwater.ticketbox.home.adapter.out.persistence.po.BannerPO;
import com.cdwater.ticketbox.home.domain.model.Banner;
import com.cdwater.ticketbox.home.domain.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BannerRepositoryImpl implements BannerRepository {

    private final BannerMapper bannerMapper;
    private final BannerConverter bannerConverter;

    @Override
    public List<Banner> findEnabled() {
        return bannerConverter.toDomain(bannerMapper.selectList(new LambdaQueryWrapper<BannerPO>()
                .eq(BannerPO::getStatus, Banner.STATUS_ENABLED)
                .orderByAsc(BannerPO::getSortOrder)
                .orderByAsc(BannerPO::getId)));
    }
}
