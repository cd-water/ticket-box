package com.cdwater.ticketbox.order.application.command;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.ResultCode;
import com.cdwater.ticketbox.common.ticket.ProductType;
import com.cdwater.ticketbox.content.api.dto.BookingSnapshotDTO;
import com.cdwater.ticketbox.inventory.api.dto.HoldCommandDTO;
import com.cdwater.ticketbox.inventory.api.dto.HoldDTO;
import com.cdwater.ticketbox.order.api.dto.BookingDetailDTO;
import com.cdwater.ticketbox.order.api.dto.CreateBookingCommandDTO;
import com.cdwater.ticketbox.order.application.port.BookingOrderNoGenerator;
import com.cdwater.ticketbox.order.application.port.CatalogPort;
import com.cdwater.ticketbox.order.application.port.InventoryPort;
import com.cdwater.ticketbox.order.application.port.PaymentPort;
import com.cdwater.ticketbox.order.application.query.BookingQueryService;
import com.cdwater.ticketbox.order.domain.model.BookingOrder;
import com.cdwater.ticketbox.order.domain.repository.BookingOrderRepository;
import com.cdwater.ticketbox.payment.api.dto.PaymentDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单 Saga 编排者：
 * 创建 = 目录快照 → 库存预留 → 本地落单；
 * 支付 = 支付 → 库存确认 → 订单转已支付；
 * 取消 = 订单关单 → 退款（已支付）→ 库存释放。
 * 远程调用不进本地事务，失败用补偿收敛。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookingCommandService {

    private static final String PAYMENT_SUCCESS = "SUCCESS";
    private static final String PAYMENT_REFUNDED = "REFUNDED";

    private final BookingOrderRepository orderRepository;
    private final BookingOrderNoGenerator orderNoGenerator;
    private final CatalogPort catalogPort;
    private final InventoryPort inventoryPort;
    private final PaymentPort paymentPort;
    private final BookingExpiryService expiryService;
    private final BookingQueryService queryService;

    public BookingDetailDTO createOrder(Long userId, CreateBookingCommandDTO command) {
        validate(command);
        BookingSnapshotDTO snapshot = catalogPort.getBookingSnapshot(
                command.getProductType(), command.getProductId(), command.getTicketTypeId());
        int quantity = quantityOf(command);

        long orderNo = orderNoGenerator.nextOrderNo();
        BookingOrder order = BookingOrder.place(orderNo, userId, snapshot, quantity, LocalDateTime.now());

        HoldDTO hold = inventoryPort.hold(holdCommandOf(command, snapshot, order, quantity));
        order.attachSeats(hold.getUnits());
        try {
            orderRepository.save(order);
        } catch (RuntimeException e) {
            safely(() -> inventoryPort.release(orderNo), "release hold " + orderNo);
            throw e;
        }
        return queryService.getDetail(userId, orderNo);
    }

    public BookingDetailDTO payOrder(Long userId, Long orderNo) {
        BookingOrder order = requireOwned(userId, orderNo);
        LocalDateTime now = LocalDateTime.now();
        if (!order.isPending()) {
            throw new BizException(ResultCode.CONFLICT, "订单状态已变更");
        }
        if (order.isExpired(now)) {
            expiryService.expireOrder(orderNo);
            throw new BizException(ResultCode.CONFLICT, "订单已关闭");
        }

        PaymentDTO payment = paymentPort.pay(orderNo, order.getTotalAmount());
        if (payment == null || !PAYMENT_SUCCESS.equals(payment.getStatus())) {
            throw new BizException(ResultCode.CONFLICT, "支付处理中，请稍后");
        }
        try {
            inventoryPort.confirm(orderNo);
        } catch (RuntimeException e) {
            safely(() -> paymentPort.refund(orderNo), "refund " + orderNo);
            throw e;
        }
        if (!orderRepository.markPaid(orderNo, now)) {
            BookingOrder latest = orderRepository.findByOrderNo(orderNo).orElse(null);
            if (latest == null || !latest.isPaid()) {
                safely(() -> inventoryPort.release(orderNo), "release hold " + orderNo);
                safely(() -> paymentPort.refund(orderNo), "refund " + orderNo);
                throw new BizException(ResultCode.CONFLICT, "订单状态已变更");
            }
        }
        return queryService.getDetail(userId, orderNo);
    }

    public BookingDetailDTO cancelOrder(Long userId, Long orderNo) {
        BookingOrder order = requireOwned(userId, orderNo);
        if (order.isPending()) {
            if (!orderRepository.markCancelled(orderNo)) {
                throw new BizException(ResultCode.CONFLICT, "订单状态已变更");
            }
            safely(() -> inventoryPort.release(orderNo), "release hold " + orderNo);
        } else if (order.isPaid()) {
            PaymentDTO refund = paymentPort.refund(orderNo);
            if (refund == null || !PAYMENT_REFUNDED.equals(refund.getStatus())) {
                throw new BizException(ResultCode.CONFLICT, "退款处理中，请稍后");
            }
            safely(() -> inventoryPort.release(orderNo), "release inventory " + orderNo);
            if (!orderRepository.markCancelledFromPaid(orderNo)) {
                throw new BizException(ResultCode.CONFLICT, "订单状态已变更");
            }
        } else {
            throw new BizException(ResultCode.CONFLICT, "订单状态已变更");
        }
        return queryService.getDetail(userId, orderNo);
    }

    public void expireOrder(Long orderNo) {
        expiryService.expireOrder(orderNo);
    }

    private HoldCommandDTO holdCommandOf(CreateBookingCommandDTO command,
                                         BookingSnapshotDTO snapshot,
                                         BookingOrder order, int quantity) {
        HoldCommandDTO hold = new HoldCommandDTO();
        hold.setProductType(command.getProductType());
        hold.setProductId(snapshot.getProductId());
        hold.setZone(snapshot.getZone());
        hold.setSeatIds(command.getProductType() == ProductType.MOVIE ? command.getSeatIds() : null);
        hold.setQuantity(quantity);
        hold.setOrderNo(order.getOrderNo());
        hold.setExpireTime(order.getExpireTime());
        return hold;
    }

    private int quantityOf(CreateBookingCommandDTO command) {
        return switch (command.getProductType()) {
            case MOVIE -> command.getSeatIds().size();
            case SHOW, PLAY -> command.getQuantity();
        };
    }

    private void validate(CreateBookingCommandDTO command) {
        if (command.getProductType() == null || command.getProductId() == null) {
            throw new BizException(ResultCode.BAD_REQUEST, "下单参数不完整");
        }
        switch (command.getProductType()) {
            case MOVIE -> {
                List<Long> seatIds = command.getSeatIds();
                if (seatIds == null || seatIds.isEmpty() || seatIds.size() > 6
                        || seatIds.stream().anyMatch(id -> id == null || id <= 0)) {
                    throw new BizException(ResultCode.BAD_REQUEST, "请选择 1-6 个有效座位");
                }
                if (seatIds.stream().distinct().count() != seatIds.size()) {
                    throw new BizException(ResultCode.BAD_REQUEST, "座位不能重复");
                }
            }
            case SHOW -> {
                if (command.getTicketTypeId() == null) {
                    throw new BizException(ResultCode.BAD_REQUEST, "请选择演出票档");
                }
                if (command.getQuantity() == null
                        || command.getQuantity() < 1 || command.getQuantity() > 6) {
                    throw new BizException(ResultCode.BAD_REQUEST, "请选择 1-6 张票");
                }
            }
            case PLAY -> {
                if (command.getQuantity() == null
                        || command.getQuantity() < 1 || command.getQuantity() > 10) {
                    throw new BizException(ResultCode.BAD_REQUEST, "请选择 1-10 份");
                }
            }
        }
    }

    private BookingOrder requireOwned(Long userId, Long orderNo) {
        return orderRepository.findByOrderNo(orderNo)
                .filter(order -> order.getUserId().equals(userId))
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "订单不存在"));
    }

    private void safely(Runnable action, String what) {
        try {
            action.run();
        } catch (RuntimeException e) {
            log.warn("compensation failed: {}", what, e);
        }
    }
}
