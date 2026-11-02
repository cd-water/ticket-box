package com.cdwater.ticketbox.play.adapter.out.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdwater.ticketbox.play.adapter.out.persistence.mapper.PlayOrderMapper;
import com.cdwater.ticketbox.play.domain.model.PlayOrder;
import com.cdwater.ticketbox.play.domain.model.PlayOrderStatus;
import com.cdwater.ticketbox.play.domain.repository.PlayOrderRepository;
import com.cdwater.ticketbox.shared.query.PageResult;
import com.cdwater.ticketbox.play.adapter.out.persistence.converter.PlayOrderConverter;
import com.cdwater.ticketbox.play.adapter.out.persistence.po.PlayOrderPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PlayOrderRepositoryImpl implements PlayOrderRepository {

    private final PlayOrderMapper playOrderMapper;
    private final PlayOrderConverter playOrderConverter;

    @Override
    public PlayOrder save(PlayOrder order) {
        playOrderMapper.insert(playOrderConverter.toPO(order));
        return order;
    }

    @Override
    public Optional<PlayOrder> findByOrderNo(Long orderNo) {
        return Optional.ofNullable(playOrderMapper.selectById(orderNo)).map(playOrderConverter::toDomain);
    }

    @Override
    public PageResult<PlayOrder> pageByUser(Long userId, int status, long page, long size) {
        Page<PlayOrderPO> result = playOrderMapper.selectPage(new Page<>(page, size),
                new LambdaQueryWrapper<PlayOrderPO>()
                        .eq(PlayOrderPO::getUserId, userId)
                        .eq(PlayOrderPO::getStatus, status)
                        .orderByDesc(PlayOrderPO::getCreateTime)
                        .orderByDesc(PlayOrderPO::getOrderNo));
        return PageResult.of(result.getTotal(), playOrderConverter.toDomain(result.getRecords()),
                result.getCurrent(), result.getSize());
    }

    @Override
    public boolean markPaid(Long orderNo, LocalDateTime payTime) {
        return playOrderMapper.markPaid(orderNo, payTime) == 1;
    }

    @Override
    public boolean markCancelled(Long orderNo) {
        return playOrderMapper.markCancelled(orderNo) == 1;
    }

    @Override
    public List<PlayOrder> findExpiredPending(LocalDateTime now, int limit) {
        int safeLimit = Math.max(1, Math.min(limit, 500));
        return playOrderConverter.toDomain(playOrderMapper.selectList(new LambdaQueryWrapper<PlayOrderPO>()
                .eq(PlayOrderPO::getStatus, PlayOrderStatus.PENDING.getCode())
                .lt(PlayOrderPO::getExpireTime, now)
                .orderByAsc(PlayOrderPO::getOrderNo)
                .last("LIMIT " + safeLimit)));
    }
}
