package com.cdwater.ticketbox.order.movie.adapter.out.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdwater.ticketbox.order.movie.domain.model.MovieOrder;
import com.cdwater.ticketbox.order.movie.domain.model.MovieOrderSeat;
import com.cdwater.ticketbox.order.movie.domain.repository.MovieOrderRepository;
import com.cdwater.ticketbox.order.movie.adapter.out.persistence.converter.MovieOrderConverter;
import com.cdwater.ticketbox.order.movie.adapter.out.persistence.converter.MovieOrderSeatConverter;
import com.cdwater.ticketbox.order.movie.adapter.out.persistence.mapper.MovieOrderMapper;
import com.cdwater.ticketbox.order.movie.adapter.out.persistence.mapper.MovieOrderSeatMapper;
import com.cdwater.ticketbox.order.movie.adapter.out.persistence.po.MovieOrderPO;
import com.cdwater.ticketbox.order.movie.adapter.out.persistence.po.MovieOrderSeatPO;
import com.cdwater.ticketbox.common.result.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MovieOrderRepositoryImpl implements MovieOrderRepository {

    private final MovieOrderMapper movieOrderMapper;
    private final MovieOrderSeatMapper movieOrderSeatMapper;
    private final MovieOrderConverter movieOrderConverter;
    private final MovieOrderSeatConverter movieOrderSeatConverter;

    @Override
    public MovieOrder save(MovieOrder order) {
        movieOrderMapper.insert(movieOrderConverter.toPO(order));
        return order;
    }

    @Override
    public Optional<MovieOrder> findByOrderNo(Long orderNo) {
        return Optional.ofNullable(movieOrderMapper.selectById(orderNo)).map(movieOrderConverter::toDomain);
    }

    @Override
    public PageResult<MovieOrder> pageByUser(Long userId, int status, long page, long size) {
        Page<MovieOrderPO> result = movieOrderMapper.selectPage(new Page<>(page, size),
                new LambdaQueryWrapper<MovieOrderPO>()
                        .eq(MovieOrderPO::getUserId, userId)
                        .eq(MovieOrderPO::getStatus, status)
                        .orderByDesc(MovieOrderPO::getCreateTime)
                        .orderByDesc(MovieOrderPO::getOrderNo));
        return PageResult.of(result.getTotal(), movieOrderConverter.toDomain(result.getRecords()),
                result.getCurrent(), result.getSize());
    }

    @Override
    public void saveSeats(List<MovieOrderSeat> seats) {
        for (MovieOrderSeat seat : seats) {
            movieOrderSeatMapper.insert(movieOrderSeatConverter.toPO(seat));
        }
    }

    @Override
    public List<MovieOrderSeat> findSeatsByOrderNo(Long orderNo) {
        return movieOrderSeatConverter.toDomain(movieOrderSeatMapper.selectList(new LambdaQueryWrapper<MovieOrderSeatPO>()
                .eq(MovieOrderSeatPO::getOrderNo, orderNo)
                .orderByAsc(MovieOrderSeatPO::getRowNum)
                .orderByAsc(MovieOrderSeatPO::getColNum)));
    }

    @Override
    public List<MovieOrder> findExpiredPending(LocalDateTime now, int limit) {
        int safeLimit = Math.max(1, Math.min(limit, 500));
        return movieOrderConverter.toDomain(movieOrderMapper.selectList(new LambdaQueryWrapper<MovieOrderPO>()
                .eq(MovieOrderPO::getStatus, 0)
                .lt(MovieOrderPO::getExpireTime, now)
                .orderByAsc(MovieOrderPO::getOrderNo)
                .last("LIMIT " + safeLimit)));
    }

    @Override
    public boolean markCancelled(Long orderNo) {
        return movieOrderMapper.markCancelled(orderNo) == 1;
    }

    @Override
    public boolean markPaid(Long orderNo, LocalDateTime payTime) {
        return movieOrderMapper.markPaid(orderNo, payTime) == 1;
    }

    @Override
    public boolean markPending(Long orderNo) {
        return movieOrderMapper.markPending(orderNo) == 1;
    }
}
