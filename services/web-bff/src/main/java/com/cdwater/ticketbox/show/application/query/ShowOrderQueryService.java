package com.cdwater.ticketbox.show.application.query;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.ResultCode;
import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.show.application.command.ShowOrderExpiryService;
import com.cdwater.ticketbox.show.application.query.view.ShowOrderDetailView;
import com.cdwater.ticketbox.show.application.query.view.ShowOrderListItemView;
import com.cdwater.ticketbox.show.application.query.view.ShowOrderSeatView;
import com.cdwater.ticketbox.show.domain.model.ShowOrder;
import com.cdwater.ticketbox.show.domain.model.ShowOrderStatus;
import com.cdwater.ticketbox.show.application.query.converter.ShowOrderViewConverter;
import com.cdwater.ticketbox.show.domain.repository.ShowOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowOrderQueryService {

    private final ShowOrderRepository orderRepository;
    private final ShowOrderExpiryService expiryService;
    private final ShowOrderViewConverter showOrderViewConverter;

    public ShowOrderDetailView getOrder(Long userId, Long orderNo) {
        ShowOrder order = orderRepository.findByOrderNo(orderNo)
                .filter(o -> o.getUserId().equals(userId))
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "订单不存在"));

        if (order.isExpired(LocalDateTime.now())) {
            expiryService.expire(orderNo);
            order = orderRepository.findByOrderNo(orderNo)
                    .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "订单不存在"));
        }

        ShowOrderDetailView view = showOrderViewConverter.toDetailView(order);
        view.setSeats(showOrderViewConverter.toSeatViews(
                orderRepository.findSeatsByOrderNo(orderNo)));
        return view;
    }

    public PageResult<ShowOrderListItemView> list(Long userId, int status, long page, long size) {
        PageResult<ShowOrder> result = orderRepository.pageByUser(userId, status, page, size);
        List<ShowOrderListItemView> records = result.getRecords().stream()
                .map(showOrderViewConverter::toListItemView)
                .toList();
        return PageResult.of(result.getTotal(), records, result.getPage(), result.getSize());
    }

}
