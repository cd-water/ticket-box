package com.cdwater.ticketbox.play.application.command;

import com.cdwater.ticketbox.play.application.port.PlayOrderNoGenerator;
import com.cdwater.ticketbox.play.application.query.PlayOrderQueryService;
import com.cdwater.ticketbox.play.application.query.view.PlayOrderDetailView;
import com.cdwater.ticketbox.play.domain.model.PlayOrder;
import com.cdwater.ticketbox.play.domain.model.PlayOrderStatus;
import com.cdwater.ticketbox.play.domain.repository.PlayOrderRepository;
import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.ResultCode;
import com.cdwater.ticketbox.content.api.ContentCatalogApi;
import com.cdwater.ticketbox.content.api.dto.PlayItemDTO;
import com.cdwater.ticketbox.inventory.api.PlayStockApi;
import com.cdwater.ticketbox.payment.api.PaymentApi;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    public PlayOrderDetailView createOrder(Long userId, CreatePlayOrderCommand command) {
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
    public PlayOrderDetailView pay(Long userId, Long orderNo) {
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
            orderRepository.markPending(orderNo);
            throw e;
        }
        return playOrderQueryService.getOrder(userId, orderNo);
    }

    @Transactional
    public PlayOrderDetailView cancel(Long userId, Long orderNo) {
        PlayOrder order = requireOwned(userId, orderNo);
        if (!orderRepository.markCancelled(orderNo)) {
            throw new BizException(ResultCode.CONFLICT, "订单状态已变更");
        }
        playStockApi.restore(order.getItemId(), order.getQuantity());
        return playOrderQueryService.getOrder(userId, orderNo);
    }

    private PlayOrder requireOwned(Long userId, Long orderNo) {
        return orderRepository.findByOrderNo(orderNo)
                .filter(o -> o.getUserId().equals(userId))
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "订单不存在"));
    }
}