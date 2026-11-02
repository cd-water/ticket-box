package com.cdwater.ticketbox.show.adapter.out.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.show.adapter.out.persistence.mapper.ShowOrderMapper;
import com.cdwater.ticketbox.show.adapter.out.persistence.mapper.ShowOrderSeatMapper;
import com.cdwater.ticketbox.show.domain.model.ShowOrder;
import com.cdwater.ticketbox.show.domain.model.ShowOrderSeat;
import com.cdwater.ticketbox.show.domain.model.ShowOrderStatus;
import com.cdwater.ticketbox.show.domain.repository.ShowOrderRepository;
import com.cdwater.ticketbox.show.adapter.out.persistence.converter.ShowOrderConverter;
import com.cdwater.ticketbox.show.adapter.out.persistence.converter.ShowOrderSeatConverter;
import com.cdwater.ticketbox.show.adapter.out.persistence.po.ShowOrderPO;
import com.cdwater.ticketbox.show.adapter.out.persistence.po.ShowOrderSeatPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ShowOrderRepositoryImpl implements ShowOrderRepository {

    private final ShowOrderMapper orderMapper;
    private final ShowOrderConverter showOrderConverter;
    private final ShowOrderSeatConverter showOrderSeatConverter;
    private final ShowOrderSeatMapper orderSeatMapper;

    @Override
    public ShowOrder save(ShowOrder order) {
        orderMapper.insert(showOrderConverter.toPO(order));
        return order;
    }

    @Override
    public Optional<ShowOrder> findByOrderNo(Long orderNo) {
        return Optional.ofNullable(orderMapper.selectById(orderNo)).map(showOrderConverter::toDomain);
    }

    @Override
    public void saveSeats(List<ShowOrderSeat> seats) {
        seats.forEach(seat -> orderSeatMapper.insert(showOrderSeatConverter.toPO(seat)));
    }

    @Override
    public List<ShowOrderSeat> findSeatsByOrderNo(Long orderNo) {
        return showOrderSeatConverter.toDomain(orderSeatMapper.selectList(new LambdaQueryWrapper<ShowOrderSeatPO>()
                .eq(ShowOrderSeatPO::getOrderNo, orderNo)
                .orderByAsc(ShowOrderSeatPO::getSeatNo)));
    }

    @Override
    public PageResult<ShowOrder> pageByUser(Long userId, int status, long page, long size) {
        Page<ShowOrderPO> result = orderMapper.selectPage(new Page<>(page, size),
                new LambdaQueryWrapper<ShowOrderPO>()
                        .eq(ShowOrderPO::getUserId, userId)
                        .eq(ShowOrderPO::getStatus, status)
                        .orderByDesc(ShowOrderPO::getCreateTime)
                        .orderByDesc(ShowOrderPO::getOrderNo));
        return PageResult.of(result.getTotal(), showOrderConverter.toDomain(result.getRecords()),
                result.getCurrent(), result.getSize());
    }

    @Override
    public boolean markPaid(Long orderNo, LocalDateTime payTime) {
        return orderMapper.markPaid(orderNo, payTime) == 1;
    }

    @Override
    public boolean markCancelled(Long orderNo) {
        return orderMapper.markCancelled(orderNo) == 1;
    }

    @Override
    public List<ShowOrder> findExpiredPending(LocalDateTime now, int limit) {
        int safeLimit = Math.max(1, Math.min(limit, 500));
        return showOrderConverter.toDomain(orderMapper.selectList(new LambdaQueryWrapper<ShowOrderPO>()
                .eq(ShowOrderPO::getStatus, ShowOrderStatus.PENDING.getCode())
                .lt(ShowOrderPO::getExpireTime, now)
                .orderByAsc(ShowOrderPO::getOrderNo)
                .last("LIMIT " + safeLimit)));
    }
}
