package com.cdwater.ticketbox.order.application.query;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.common.result.ResultCode;
import com.cdwater.ticketbox.common.ticket.ProductType;
import com.cdwater.ticketbox.order.api.dto.BookingDetailDTO;
import com.cdwater.ticketbox.order.api.dto.BookingListItemDTO;
import com.cdwater.ticketbox.order.application.command.BookingExpiryService;
import com.cdwater.ticketbox.order.application.query.converter.BookingViewConverter;
import com.cdwater.ticketbox.order.domain.model.BookingOrder;
import com.cdwater.ticketbox.order.domain.repository.BookingOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingQueryService {

    private final BookingOrderRepository orderRepository;
    private final BookingExpiryService expiryService;
    private final BookingViewConverter viewConverter;

    public BookingDetailDTO getDetail(Long userId, Long orderNo) {
        BookingOrder order = loadOwned(userId, orderNo);
        if (order.isExpired(LocalDateTime.now())) {
            expiryService.expireOrder(orderNo);
            order = loadOwned(userId, orderNo);
        }
        return viewConverter.toDetail(order);
    }

    public PageResult<BookingListItemDTO> list(Long userId, ProductType productType, int status,
                                               long page, long size) {
        PageResult<BookingOrder> result = orderRepository.pageByUserAndProductType(
                userId, productType, status, page, size);
        List<BookingListItemDTO> records = result.getRecords().stream()
                .map(viewConverter::toListItem)
                .toList();
        return PageResult.of(result.getTotal(), records, result.getPage(), result.getSize());
    }

    private BookingOrder loadOwned(Long userId, Long orderNo) {
        return orderRepository.findByOrderNo(orderNo)
                .filter(order -> order.getUserId().equals(userId))
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "订单不存在"));
    }
}
