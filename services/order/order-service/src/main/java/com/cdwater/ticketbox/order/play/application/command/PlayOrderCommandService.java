package com.cdwater.ticketbox.order.play.application.command;

import com.cdwater.ticketbox.order.play.application.port.PlayOrderNoGenerator;
import com.cdwater.ticketbox.order.play.application.query.PlayOrderQueryService;
import com.cdwater.ticketbox.order.api.dto.PlayOrderDetailDTO;
import com.cdwater.ticketbox.order.play.domain.model.PlayOrder;
import com.cdwater.ticketbox.order.play.domain.model.PlayOrderStatus;
import com.cdwater.ticketbox.order.play.domain.repository.PlayOrderRepository;
import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.order.api.dto.CreatePlayOrderCommandDTO;
import com.cdwater.ticketbox.common.result.ResultCode;
import com.cdwater.ticketbox.content.api.ContentCatalogApi;
import com.cdwater.ticketbox.content.api.dto.PlayItemDTO;
import com.cdwater.ticketbox.inventory.api.PlayStockApi;
import com.cdwater.ticketbox.payment.api.PaymentApi;
import com.cdwater.ticketbox.payment.api.dto.PaymentDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayOrderCommandService {

    private final PlayOrderRepository orderRepository;
    private final PlayOrderNoGenerator orderNoGenerator;
    private final PlayOrderExpiryService playOrderExpiryService;
    private final PlayOrderQueryService playOrderQueryService;

    @DubboReference(check = false, timeout = 3000)
    private ContentCatalogApi contentCatalogApi;

    @DubboReference(check = false, timeout = 3000)
    private PlayStockApi playStockApi;

    @DubboReference(check = false, timeout = 3000)
    private PaymentApi paymentApi;

    @Transactional
    public PlayOrderDetailDTO createOrder(Long userId, CreatePlayOrderCommandDTO command) {
        PlayItemDTO item = contentCatalogApi.getPlayItem(command.getItemId());
        if (!playStockApi.deduct(item.getId(), command.getQuantity())) {
            throw new BizException(ResultCode.CONFLICT, "余票不足");
        }

        PlayOrder order = PlayOrder.place(orderNoGenerator.nextOrderNo(), userId,
                item, command.getQuantity(), LocalDateTime.now());
        try {
            orderRepository.save(order);
        } catch (RuntimeException e) {
            playStockApi.restore(item.getId(), command.getQuantity());
            throw e;
        }

        return playOrderQueryService.getOrder(userId, order.getOrderNo());
    }

    @Transactional
    public PlayOrderDetailDTO pay(Long userId, Long orderNo) {
        PlayOrder order = requireOwned(userId, orderNo);
        LocalDateTime now = LocalDateTime.now();
        if (!order.isPending()) {
            throw new BizException(ResultCode.CONFLICT, "订单状态已变更");
        }
        if (order.isExpired(now)) {
            playOrderExpiryService.expire(orderNo);
            throw new BizException(ResultCode.CONFLICT, "订单已关闭");
        }
        if (!orderRepository.markPaid(orderNo, now)) {
            throw new BizException(ResultCode.CONFLICT, "订单状态已变更");
        }
        try {
            paymentApi.create(orderNo, order.getTotalAmount(), "MOCK");
        } catch (RuntimeException e) {
            safely(() -> orderRepository.markPending(orderNo), "markPending " + orderNo);
            throw e;
        }
        return playOrderQueryService.getOrder(userId, orderNo);
    }

    @Transactional
    public PlayOrderDetailDTO cancel(Long userId, Long orderNo) {
        PlayOrder order = requireOwned(userId, orderNo);
        PaymentDTO payment = paymentApi.queryByOrderNo(orderNo);
        if (payment != null && "SUCCESS".equals(payment.getStatus())) {
            throw new BizException(ResultCode.CONFLICT, "订单支付处理中，请稍后");
        }
        if (!orderRepository.markCancelled(orderNo)) {
            throw new BizException(ResultCode.CONFLICT, "订单状态已变更");
        }
        safely(() -> playStockApi.restore(order.getItemId(), order.getQuantity()),
                "restore play stock " + orderNo);
        return playOrderQueryService.getOrder(userId, orderNo);
    }

    private void safely(Runnable action, String what) {
        try {
            action.run();
        } catch (RuntimeException ex) {
            log.warn("compensation failed: {}", what, ex);
        }
    }

    private PlayOrder requireOwned(Long userId, Long orderNo) {
        return orderRepository.findByOrderNo(orderNo)
                .filter(o -> o.getUserId().equals(userId))
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "订单不存在"));
    }
}