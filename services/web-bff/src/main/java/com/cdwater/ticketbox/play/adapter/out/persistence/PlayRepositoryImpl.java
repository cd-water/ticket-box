package com.cdwater.ticketbox.play.adapter.out.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdwater.ticketbox.play.adapter.out.persistence.mapper.PlayItemMapper;
import com.cdwater.ticketbox.play.domain.model.PlayItem;
import com.cdwater.ticketbox.play.domain.repository.PlayRepository;
import com.cdwater.ticketbox.persistence.LikeUtils;
import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.play.adapter.out.persistence.converter.PlayItemConverter;
import com.cdwater.ticketbox.play.adapter.out.persistence.po.PlayItemPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PlayRepositoryImpl implements PlayRepository {

    private final PlayItemMapper playItemMapper;
    private final PlayItemConverter playItemConverter;

    @Override
    public PageResult<PlayItem> page(String keyword, long page, long size) {
        LambdaQueryWrapper<PlayItemPO> wrapper = new LambdaQueryWrapper<PlayItemPO>()
                .eq(PlayItemPO::getStatus, PlayItem.STATUS_ON_SALE)
                .orderByDesc(PlayItemPO::getId);
        String trimmed = keyword == null ? "" : keyword.trim();
        if (!trimmed.isEmpty()) {
            wrapper.like(PlayItemPO::getTitle, LikeUtils.escape(trimmed));
        }
        Page<PlayItemPO> result = playItemMapper.selectPage(new Page<>(page, size), wrapper);
        return PageResult.of(result.getTotal(), playItemConverter.toDomain(result.getRecords()),
                result.getCurrent(), result.getSize());
    }

    @Override
    public Optional<PlayItem> findById(Long id) {
        return Optional.ofNullable(playItemMapper.selectById(id)).map(playItemConverter::toDomain);
    }
}
