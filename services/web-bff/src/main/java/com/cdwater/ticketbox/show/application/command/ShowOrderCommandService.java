package com.cdwater.ticketbox.show.application.command;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.ResultCode;
import com.cdwater.ticketbox.show.application.port.ShowOrderNoGenerator;
import com.cdwater.ticketbox.show.application.query.ShowOrderQueryService;
import com.cdwater.ticketbox.show.application.query.view.ShowOrderDetailView;
import com.cdwater.ticketbox.show.domain.model.ShowOrder;
import com.cdwater.ticketbox.show.domain.model.ShowOrderSeat;
import com.cdwater.ticketbox.show.domain.model.ShowOrderStatus;
import com.cdwater.ticketbox.show.domain.repository.ShowOrderRepository;
import com.cdwater.ticketbox.content.api.ContentCatalogApi;
import com.cdwater.ticketbox.content.api.dto.ShowBookingDTO;
import com.cdwater.ticketbox.inventory.api.ShowSeatInventoryApi;
import com.cdwater.ticketbox.inventory.api.dto.ShowSeatSnapshotDTO;
import com.cdwater.ticketbox.payment.api.PaymentApi;
import com.cdwater.ticketbox.payment.api.dto.PaymentDTO;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowOrderCommandService {

    private final ShowOrderRepository orderRepository;
    private final ShowOrderNoGenerator orderNoGenerator;
    private final ShowOrderExpiryService expiryService;
    private final ShowOrderQueryService showOrderQueryService;

    @DubboReference(check = false, timeout = 3000)
    private ContentCatalogApi contentCatalogApi;

    @DubboReference(check = false, timeout = 3000)
    private ShowSeatInventoryApi showSeatInventoryApi;

    @DubboReference(check = false, timeout = 3000)
    private PaymentApi paymentApi;

    @Transactional
    public ShowOrderDetailView createOrder(Long userId, CreateShowOrderCommand command) {
        LocalDateTime now = LocalDateTime.now();
        ShowBookingDTO booking = contentCatalogApi.getShowBooking(
                command.getShowId(), command.getTicketTypeId());

        long available = showSeatInventoryApi.countAvailable(booking.getShowId())
                .getOrDefault(booking.getZone(), 0L);
        if (available < command.getQuantity()) {
            throw new BizException(ResultCode.CONFLICT, "该区域余票不足");
        }

        ShowOrder order = ShowOrder.place(orderNoGenerator.nextOrderNo(), userId,
                booking, command.getQuantity(), now);
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
        PaymentDTO payment;
        try {
            payment = paymentApi.create(orderNo, order.getTotalAmount(), "MOCK");
        } catch (RuntimeException e) {
            orderRepository.markPending(orderNo);
            throw e;
        }
        try {
            List<ShowSeatSnapshotDTO> picked = showSeatInventoryApi.sell(
                    order.getShowId(), order.getZone(), order.getQuantity(), orderNo);
            orderRepository.saveSeats(picked.stream().map(seat -> {
                ShowOrderSeat orderSeat = new ShowOrderSeat();
                orderSeat.setOrderNo(orderNo);
                orderSeat.setSeatId(seat.getId());
                orderSeat.setZone(seat.getZone());
                orderSeat.setSeatNo(seat.getSeatNo());
                orderSeat.setPrice(order.getUnitPrice());
                return orderSeat;
            }).toList());
        } catch (RuntimeException e) {
            showSeatInventoryApi.rollbackSale(orderNo);
            orderRepository.markPending(orderNo);
            paymentApi.close(payment.getPaymentNo());
            throw e;
        }

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