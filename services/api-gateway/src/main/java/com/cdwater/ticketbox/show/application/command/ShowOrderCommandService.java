package com.cdwater.ticketbox.show.application.command;

import com.cdwater.ticketbox.shared.error.BizException;
import com.cdwater.ticketbox.shared.error.ResultCode;
import com.cdwater.ticketbox.show.application.port.ShowOrderNoGenerator;
import com.cdwater.ticketbox.show.application.query.ShowOrderQueryService;
import com.cdwater.ticketbox.show.application.query.view.ShowOrderDetailView;
import com.cdwater.ticketbox.show.domain.model.Show;
import com.cdwater.ticketbox.show.domain.model.ShowOrder;
import com.cdwater.ticketbox.show.domain.model.ShowOrderSeat;
import com.cdwater.ticketbox.show.domain.model.ShowOrderStatus;
import com.cdwater.ticketbox.show.domain.model.ShowSeat;
import com.cdwater.ticketbox.show.domain.model.ShowTicketType;
import com.cdwater.ticketbox.show.domain.repository.ShowOrderRepository;
import com.cdwater.ticketbox.show.domain.repository.ShowRepository;
import com.cdwater.ticketbox.show.domain.repository.ShowSeatInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowOrderCommandService {

    private final ShowRepository showRepository;
    private final ShowOrderRepository orderRepository;
    private final ShowSeatInventoryRepository seatInventoryRepository;
    private final ShowOrderNoGenerator orderNoGenerator;
    private final ShowOrderExpiryService expiryService;
    private final ShowOrderQueryService showOrderQueryService;

    @Transactional
    public ShowOrderDetailView createOrder(Long userId, CreateShowOrderCommand command) {
        LocalDateTime now = LocalDateTime.now();
        Show show = showRepository.findById(command.getShowId())
                .filter(s -> s.isUpcoming(now))
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "演出不存在或已开演"));
        ShowTicketType ticketType = showRepository
                .findTicketType(show.getId(), command.getTicketTypeId())
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "票档不存在或已停售"));

        long available = showRepository.countAvailableSeatsByZone(show.getId())
                .getOrDefault(ticketType.getZone(), 0L);
        if (available < command.getQuantity()) {
            throw new BizException(ResultCode.CONFLICT, "该区域余票不足");
        }

        ShowOrder order = ShowOrder.place(orderNoGenerator.nextOrderNo(), userId,
                show.getId(), show.getTitle(), show.getVenueName(), show.getVenueAddress(),
                show.getStartTime(), ticketType, command.getQuantity(), now);
        orderRepository.save(order);

        return showOrderQueryService.getOrder(userId, order.getOrderNo());
    }

    @Transactional
    public ShowOrderDetailView pay(Long userId, Long orderNo) {
        ShowOrder order = requireOwned(userId, orderNo);
        LocalDateTime now = LocalDateTime.now();
        if (!order.isPending()) {
            throw new BizException(ResultCode.CONFLICT, "订单状态已变更");
        }
        if (order.isExpired(now)) {
            expiryService.expire(orderNo);
            throw new BizException(ResultCode.CONFLICT, "订单已关闭");
        }
        if (!orderRepository.markPaid(orderNo, now)) {
            throw new BizException(ResultCode.CONFLICT, "订单状态已变更");
        }

        List<ShowSeat> seats = seatInventoryRepository.lockAvailableSeats(order.getShowId(), order.getZone());
        if (seats.size() < order.getQuantity()) {
            throw new BizException(ResultCode.CONFLICT, "该区域余票不足");
        }
        List<ShowSeat> picked = SeatPicker.pick(seats, order.getQuantity());
        seatInventoryRepository.markSold(
                picked.stream().map(ShowSeat::getId).toList(), orderNo);
        orderRepository.saveSeats(picked.stream().map(seat -> {
            ShowOrderSeat orderSeat = new ShowOrderSeat();
            orderSeat.setOrderNo(orderNo);
            orderSeat.setSeatId(seat.getId());
            orderSeat.setZone(seat.getZone());
            orderSeat.setSeatNo(seat.getSeatNo());
            orderSeat.setPrice(order.getUnitPrice());
            return orderSeat;
        }).toList());

        return showOrderQueryService.getOrder(userId, orderNo);
    }

    @Transactional
    public ShowOrderDetailView cancel(Long userId, Long orderNo) {
        requireOwned(userId, orderNo);
        if (!orderRepository.markCancelled(orderNo)) {
            throw new BizException(ResultCode.CONFLICT, "订单状态已变更");
        }
        return showOrderQueryService.getOrder(userId, orderNo);
    }

    @Transactional
    public void expire(Long orderNo) {
        expiryService.expire(orderNo);
    }

    private ShowOrder requireOwned(Long userId, Long orderNo) {
        return orderRepository.findByOrderNo(orderNo)
                .filter(o -> o.getUserId().equals(userId))
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "订单不存在"));
    }
}
