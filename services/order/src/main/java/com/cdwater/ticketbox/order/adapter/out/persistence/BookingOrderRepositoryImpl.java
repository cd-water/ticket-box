package com.cdwater.ticketbox.order.adapter.out.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.common.ticket.ProductType;
import com.cdwater.ticketbox.order.adapter.out.persistence.mapper.BookingLineMapper;
import com.cdwater.ticketbox.order.adapter.out.persistence.mapper.BookingOrderMapper;
import com.cdwater.ticketbox.order.adapter.out.persistence.mapper.BookingSeatMapper;
import com.cdwater.ticketbox.order.adapter.out.persistence.po.BookingLinePO;
import com.cdwater.ticketbox.order.adapter.out.persistence.po.BookingOrderPO;
import com.cdwater.ticketbox.order.adapter.out.persistence.po.BookingSeatPO;
import com.cdwater.ticketbox.order.domain.model.BookingLine;
import com.cdwater.ticketbox.order.domain.model.BookingOrder;
import com.cdwater.ticketbox.order.domain.model.BookingSeat;
import com.cdwater.ticketbox.order.domain.model.BookingStatus;
import com.cdwater.ticketbox.order.domain.repository.BookingOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BookingOrderRepositoryImpl implements BookingOrderRepository {

    private final BookingOrderMapper orderMapper;
    private final BookingLineMapper lineMapper;
    private final BookingSeatMapper seatMapper;

    @Override
    @Transactional
    public BookingOrder save(BookingOrder order) {
        orderMapper.insert(toPO(order));
        lineMapper.insert(toPO(order.getLine()));
        order.getSeats().forEach(seat -> seatMapper.insert(toPO(seat)));
        return order;
    }

    @Override
    public Optional<BookingOrder> findByOrderNo(Long orderNo) {
        BookingOrderPO po = orderMapper.selectById(orderNo);
        if (po == null) {
            return Optional.empty();
        }
        BookingOrder order = toDomain(po);
        BookingLinePO linePO = lineMapper.selectById(orderNo);
        if (linePO != null) {
            order.setLine(toDomain(linePO));
        }
        order.setSeats(seatMapper.selectList(new LambdaQueryWrapper<BookingSeatPO>()
                        .eq(BookingSeatPO::getOrderNo, orderNo)
                        .orderByAsc(BookingSeatPO::getSeatId))
                .stream().map(this::toDomain).toList());
        return Optional.of(order);
    }

    @Override
    public PageResult<BookingOrder> pageByUserAndProductType(Long userId, ProductType productType,
                                                             int status, long page, long size) {
        Page<BookingOrderPO> result = orderMapper.selectPage(new Page<>(page, size),
                new LambdaQueryWrapper<BookingOrderPO>()
                        .eq(BookingOrderPO::getUserId, userId)
                        .eq(BookingOrderPO::getProductType, productType.name())
                        .eq(BookingOrderPO::getStatus, status)
                        .orderByDesc(BookingOrderPO::getCreateTime)
                        .orderByDesc(BookingOrderPO::getOrderNo));
        List<BookingOrder> orders = result.getRecords().stream().map(this::toDomain).toList();
        attachLines(orders);
        return PageResult.of(result.getTotal(), orders, result.getCurrent(), result.getSize());
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
    public boolean markCancelledFromPaid(Long orderNo) {
        return orderMapper.markCancelledFromPaid(orderNo) == 1;
    }

    @Override
    public List<BookingOrder> findExpiredPending(LocalDateTime now, int limit) {
        int safeLimit = Math.max(1, Math.min(limit, 500));
        return orderMapper.selectList(new LambdaQueryWrapper<BookingOrderPO>()
                        .eq(BookingOrderPO::getStatus, BookingStatus.PENDING.getCode())
                        .lt(BookingOrderPO::getExpireTime, now)
                        .orderByAsc(BookingOrderPO::getOrderNo)
                        .last("LIMIT " + safeLimit))
                .stream().map(this::toDomain).toList();
    }

    private void attachLines(List<BookingOrder> orders) {
        if (orders.isEmpty()) {
            return;
        }
        List<Long> orderNos = orders.stream().map(BookingOrder::getOrderNo).toList();
        Map<Long, BookingLine> lines = lineMapper.selectList(new LambdaQueryWrapper<BookingLinePO>()
                        .in(BookingLinePO::getOrderNo, orderNos))
                .stream().collect(Collectors.toMap(BookingLinePO::getOrderNo, this::toDomain));
        orders.forEach(order -> order.setLine(lines.get(order.getOrderNo())));
    }

    private BookingOrderPO toPO(BookingOrder order) {
        BookingOrderPO po = new BookingOrderPO();
        po.setOrderNo(order.getOrderNo());
        po.setUserId(order.getUserId());
        po.setProductType(order.getProductType().name());
        po.setStatus(order.getStatus());
        po.setTotalAmount(order.getTotalAmount());
        po.setExpireTime(order.getExpireTime());
        po.setPayTime(order.getPayTime());
        return po;
    }

    private BookingOrder toDomain(BookingOrderPO po) {
        BookingOrder order = new BookingOrder();
        order.setOrderNo(po.getOrderNo());
        order.setUserId(po.getUserId());
        order.setProductType(ProductType.valueOf(po.getProductType()));
        order.setStatus(po.getStatus());
        order.setTotalAmount(po.getTotalAmount());
        order.setExpireTime(po.getExpireTime());
        order.setPayTime(po.getPayTime());
        order.setCreateTime(po.getCreateTime());
        order.setUpdateTime(po.getUpdateTime());
        return order;
    }

    private BookingLinePO toPO(BookingLine line) {
        BookingLinePO po = new BookingLinePO();
        po.setOrderNo(line.getOrderNo());
        po.setProductId(line.getProductId());
        po.setTicketTypeId(line.getTicketTypeId());
        po.setTitle(line.getTitle());
        po.setVenueName(line.getVenueName());
        po.setVenueAddress(line.getVenueAddress());
        po.setHallName(line.getHallName());
        po.setTicketTypeName(line.getTicketTypeName());
        po.setZone(line.getZone());
        po.setStartTime(line.getStartTime());
        po.setEndTime(line.getEndTime());
        po.setUnitPrice(line.getUnitPrice());
        po.setQuantity(line.getQuantity());
        return po;
    }

    private BookingLine toDomain(BookingLinePO po) {
        BookingLine line = new BookingLine();
        line.setOrderNo(po.getOrderNo());
        line.setProductId(po.getProductId());
        line.setTicketTypeId(po.getTicketTypeId());
        line.setTitle(po.getTitle());
        line.setVenueName(po.getVenueName());
        line.setVenueAddress(po.getVenueAddress());
        line.setHallName(po.getHallName());
        line.setTicketTypeName(po.getTicketTypeName());
        line.setZone(po.getZone());
        line.setStartTime(po.getStartTime());
        line.setEndTime(po.getEndTime());
        line.setUnitPrice(po.getUnitPrice());
        line.setQuantity(po.getQuantity());
        return line;
    }

    private BookingSeatPO toPO(BookingSeat seat) {
        BookingSeatPO po = new BookingSeatPO();
        po.setOrderNo(seat.getOrderNo());
        po.setSeatId(seat.getSeatId());
        po.setZone(seat.getZone());
        po.setSeatNo(seat.getSeatNo());
        po.setRowNum(seat.getRowNum());
        po.setColNum(seat.getColNum());
        po.setPrice(seat.getPrice());
        return po;
    }

    private BookingSeat toDomain(BookingSeatPO po) {
        BookingSeat seat = new BookingSeat();
        seat.setOrderNo(po.getOrderNo());
        seat.setSeatId(po.getSeatId());
        seat.setZone(po.getZone());
        seat.setSeatNo(po.getSeatNo());
        seat.setRowNum(po.getRowNum());
        seat.setColNum(po.getColNum());
        seat.setPrice(po.getPrice());
        return seat;
    }
}
