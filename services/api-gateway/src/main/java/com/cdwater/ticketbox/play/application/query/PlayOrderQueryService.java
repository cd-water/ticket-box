package com.cdwater.ticketbox.play.application.query;

import com.cdwater.ticketbox.play.application.command.PlayOrderExpiryService;
import com.cdwater.ticketbox.play.application.query.view.PlayOrderDetailView;
import com.cdwater.ticketbox.play.application.query.view.PlayOrderListItemView;
import com.cdwater.ticketbox.play.domain.model.PlayOrder;
import com.cdwater.ticketbox.play.domain.model.PlayOrderStatus;
import com.cdwater.ticketbox.play.application.query.converter.PlayOrderViewConverter;
import com.cdwater.ticketbox.play.domain.repository.PlayOrderRepository;
import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.ResultCode;
import com.cdwater.ticketbox.common.result.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayOrderQueryService {

    private final PlayOrderRepository orderRepository;
    private final PlayOrderExpiryService expiryService;
    private final PlayOrderViewConverter playOrderViewConverter;

    public PlayOrderDetailView getOrder(Long userId, Long orderNo) {
        PlayOrder order = orderRepository.findByOrderNo(orderNo)
                .filter(o -> o.getUserId().equals(userId))
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "订单不存在"));

        if (order.isExpired(LocalDateTime.now())) {
            expiryService.expire(orderNo);
            order = orderRepository.findByOrderNo(orderNo)
                    .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "订单不存在"));
        }

        return playOrderViewConverter.toDetailView(order);
    }

    public PageResult<PlayOrderListItemView> list(Long userId, int status, long page, long size) {
        PageResult<PlayOrder> result = orderRepository.pageByUser(userId, status, page, size);
        List<PlayOrderListItemView> records = result.getRecords().stream()
                .map(playOrderViewConverter::toListItemView)
                .toList();
        return PageResult.of(result.getTotal(), records, result.getPage(), result.getSize());
    }

}
