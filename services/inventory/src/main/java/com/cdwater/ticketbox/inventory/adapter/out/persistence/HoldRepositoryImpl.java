package com.cdwater.ticketbox.inventory.adapter.out.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cdwater.ticketbox.common.ticket.ProductType;
import com.cdwater.ticketbox.inventory.adapter.out.persistence.mapper.HoldMapper;
import com.cdwater.ticketbox.inventory.adapter.out.persistence.po.HoldPO;
import com.cdwater.ticketbox.inventory.domain.model.Hold;
import com.cdwater.ticketbox.inventory.domain.model.HoldStatus;
import com.cdwater.ticketbox.inventory.domain.repository.HoldRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HoldRepositoryImpl implements HoldRepository {

    private final HoldMapper holdMapper;

    @Override
    public Optional<Hold> findByOrderNo(Long orderNo) {
        return Optional.ofNullable(holdMapper.selectById(orderNo)).map(this::toDomain);
    }

    @Override
    public void insert(Hold hold) {
        holdMapper.insert(toPO(hold));
    }

    @Override
    public int markConfirmed(Long orderNo) {
        return holdMapper.markConfirmed(orderNo);
    }

    @Override
    public int markReleased(Long orderNo) {
        return holdMapper.markReleased(orderNo);
    }

    @Override
    public List<Hold> findExpiredHeld(LocalDateTime now, int limit) {
        int safeLimit = Math.max(1, Math.min(limit, 500));
        return holdMapper.selectList(new LambdaQueryWrapper<HoldPO>()
                        .eq(HoldPO::getStatus, HoldStatus.HELD.getCode())
                        .lt(HoldPO::getExpireTime, now)
                        .orderByAsc(HoldPO::getExpireTime)
                        .last("LIMIT " + safeLimit))
                .stream().map(this::toDomain).toList();
    }

    private Hold toDomain(HoldPO po) {
        Hold hold = new Hold();
        hold.setOrderNo(po.getOrderNo());
        hold.setProductType(ProductType.valueOf(po.getProductType()));
        hold.setProductId(po.getProductId());
        hold.setZone(po.getZone());
        hold.setQuantity(po.getQuantity());
        hold.setStatus(po.getStatus());
        hold.setExpireTime(po.getExpireTime());
        hold.setCreateTime(po.getCreateTime());
        hold.setUpdateTime(po.getUpdateTime());
        return hold;
    }

    private HoldPO toPO(Hold hold) {
        HoldPO po = new HoldPO();
        po.setOrderNo(hold.getOrderNo());
        po.setProductType(hold.getProductType().name());
        po.setProductId(hold.getProductId());
        po.setZone(hold.getZone());
        po.setQuantity(hold.getQuantity());
        po.setStatus(hold.getStatus());
        po.setExpireTime(hold.getExpireTime());
        return po;
    }
}
