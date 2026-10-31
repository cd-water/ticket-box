package com.cdwater.ticketbox.booking.application.service;

import com.cdwater.ticketbox.booking.api.dto.CreateOrderDTO;
import com.cdwater.ticketbox.booking.api.vo.OrderDetailVO;
import com.cdwater.ticketbox.booking.api.vo.OrderSeatVO;
import com.cdwater.ticketbox.booking.application.port.OrderNoGenerator;
import com.cdwater.ticketbox.booking.domain.model.Order;
import com.cdwater.ticketbox.booking.domain.model.OrderSeat;
import com.cdwater.ticketbox.booking.domain.model.OrderStatus;
import com.cdwater.ticketbox.booking.domain.repository.OrderRepository;
import com.cdwater.ticketbox.cinema.application.dto.ReservedSeat;
import com.cdwater.ticketbox.cinema.application.dto.ScreeningBookingInfo;
import com.cdwater.ticketbox.cinema.application.service.ScreeningQueryService;
import com.cdwater.ticketbox.cinema.application.service.SeatReservationService;
import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.ResultCode;
import com.cdwater.ticketbox.movie.application.query.MovieBrief;
import com.cdwater.ticketbox.movie.application.query.MovieQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderApplicationService {

    private final OrderRepository orderRepository;
    private final OrderNoGenerator orderNoGenerator;
    private final ScreeningQueryService screeningQueryService;
    private final SeatReservationService seatReservationService;
    private final MovieQueryService movieQueryService;

    @Transactional
    public OrderDetailVO createOrder(Long userId, CreateOrderDTO dto) {
        List<Long> rawSeatIds = dto.getSeatIds();
        if (rawSeatIds == null || rawSeatIds.isEmpty() || rawSeatIds.size() > 6
                || rawSeatIds.stream().anyMatch(id -> id == null || id <= 0)) {
            throw new BizException(ResultCode.BAD_REQUEST, "请选择 1-6 个有效座位");
        }
        List<Long> seatIds = rawSeatIds.stream().distinct().toList();
        if (seatIds.size() != rawSeatIds.size()) {
            throw new BizException(ResultCode.BAD_REQUEST, "座位不能重复");
        }

        ScreeningBookingInfo info = screeningQueryService.getBookingInfo(dto.getScreeningId());
        if (info.status() == null || info.status() != 1
                || !info.startTime().isAfter(LocalDateTime.now())) {
            throw new BizException(ResultCode.NOT_FOUND, "排场不存在或已不可售");
        }
        movieQueryService.findById(info.movieId())
                .filter(m -> m.status() != null && m.status() == 1)
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "电影不存在或已下架"));

        LocalDateTime expireTime = LocalDateTime.now().plusMinutes(15);
        Order order = new Order();
        order.setOrderNo(orderNoGenerator.nextOrderNo());
        order.setUserId(userId);
        order.setScreeningId(info.screeningId());
        order.setTotalAmount(info.price().multiply(BigDecimal.valueOf(seatIds.size())));
        order.setStatus(OrderStatus.PENDING.getCode());
        order.setExpireTime(expireTime);
        orderRepository.save(order);

        List<ReservedSeat> snapshots = seatReservationService.lockSeats(
                info.screeningId(), seatIds, order.getId(), expireTime);
        List<OrderSeat> orderSeats = snapshots.stream().map(s -> {
            OrderSeat seat = new OrderSeat();
            seat.setOrderId(order.getId());
            seat.setSeatId(s.seatId());
            seat.setRowNum(s.rowNum());
            seat.setColNum(s.colNum());
            seat.setPrice(info.price());
            return seat;
        }).toList();
        orderRepository.saveSeats(orderSeats);

        return getOrder(userId, order.getId());
    }

    @Transactional
    public OrderDetailVO getOrder(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .filter(o -> o.getUserId().equals(userId))
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "订单不存在"));

        if (order.getStatus() == OrderStatus.PENDING.getCode()
                && !order.getExpireTime().isAfter(LocalDateTime.now())) {
            if (orderRepository.markTimeout(orderId)) {
                seatReservationService.releaseByOrder(orderId);
            }
            order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "订单不存在"));
        }

        ScreeningBookingInfo info = screeningQueryService.getBookingInfo(order.getScreeningId());
        MovieBrief movie = movieQueryService.findById(info.movieId())
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "电影不存在"));
        List<OrderSeat> seats = orderRepository.findSeatsByOrderId(orderId);

        OrderDetailVO vo = new OrderDetailVO();
        vo.setId(order.getId());
        vo.setOrderNo(String.valueOf(order.getOrderNo()));
        vo.setStatus(order.getStatus());
        vo.setTotalAmount(order.getTotalAmount());
        vo.setExpireTime(order.getExpireTime());
        vo.setCreateTime(order.getCreateTime());
        vo.setCinemaName(info.cinemaName());
        vo.setMovieTitle(movie.title());
        vo.setHallName(info.hallName());
        vo.setStartTime(info.startTime());
        vo.setEndTime(info.endTime());
        vo.setSeats(seats.stream()
                .map(s -> new OrderSeatVO(s.getSeatId(), s.getRowNum(), s.getColNum(), s.getPrice()))
                .toList());
        return vo;
    }

    @Transactional
    public void expireOrder(Long orderId) {
        if (orderRepository.markTimeout(orderId)) {
            seatReservationService.releaseByOrder(orderId);
        }
    }
}
