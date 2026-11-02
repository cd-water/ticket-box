package com.cdwater.ticketbox.order.application.command;

import com.cdwater.ticketbox.order.application.port.InventoryPort;
import com.cdwater.ticketbox.order.application.port.PaymentPort;
import com.cdwater.ticketbox.order.domain.model.BookingOrder;
import com.cdwater.ticketbox.order.domain.repository.BookingOrderRepository;
import com.cdwater.ticketbox.payment.api.dto.PaymentDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** 待支付订单超时关单；支付已成功的订单跳过，交给对账/回调收敛。 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookingExpiryService {

    private static final String PAYMENT_SUCCESS = "SUCCESS";

    private final BookingOrderRepository orderRepository;
    private final InventoryPort inventoryPort;
    private final PaymentPort paymentPort;

    public void expireOrder(Long orderNo) {
        BookingOrder order = orderRepository.findByOrderNo(orderNo).orElse(null);
        if (order == null || !order.isPending()) {
            return;
        }
        PaymentDTO payment = paymentPort.find(orderNo);
        if (payment != null && PAYMENT_SUCCESS.equals(payment.getStatus())) {
            log.warn("skip expiring paid order {}", orderNo);
            return;
        }
        if (orderRepository.markCancelled(orderNo)) {
            try {
                inventoryPort.release(orderNo);
            } catch (RuntimeException e) {
                log.warn("release hold failed, orderNo={}", orderNo, e);
            }
        }
    }
}
