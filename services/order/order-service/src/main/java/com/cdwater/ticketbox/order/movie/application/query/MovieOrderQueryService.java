package com.cdwater.ticketbox.order.movie.application.query;

import com.cdwater.ticketbox.order.movie.application.query.converter.MovieOrderViewConverter;
import com.cdwater.ticketbox.order.api.dto.MovieOrderDetailDTO;
import com.cdwater.ticketbox.order.api.dto.MovieOrderListItemDTO;
import com.cdwater.ticketbox.order.movie.domain.model.MovieOrder;
import com.cdwater.ticketbox.order.movie.domain.repository.MovieOrderRepository;
import com.cdwater.ticketbox.order.movie.application.command.MovieOrderExpiryService;
import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.ResultCode;
import com.cdwater.ticketbox.common.result.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieOrderQueryService {

    private final MovieOrderRepository orderRepository;
    private final MovieOrderExpiryService orderExpiryService;
    private final MovieOrderViewConverter movieOrderViewConverter;

    public MovieOrderDetailDTO getOrder(Long userId, Long orderNo) {
        MovieOrder order = orderRepository.findByOrderNo(orderNo)
                .filter(o -> o.getUserId().equals(userId))
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "订单不存在"));

        if (order.isExpired(LocalDateTime.now())) {
            orderExpiryService.expireOrder(orderNo);
            order = orderRepository.findByOrderNo(orderNo)
                    .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "订单不存在"));
        }

        MovieOrderDetailDTO view = movieOrderViewConverter.toDetailView(order);
        view.setSeats(movieOrderViewConverter.toSeatViews(
                orderRepository.findSeatsByOrderNo(orderNo)));
        return view;
    }

    public PageResult<MovieOrderListItemDTO> list(Long userId, int status, long page, long size) {
        PageResult<MovieOrder> result = orderRepository.pageByUser(userId, status, page, size);
        List<MovieOrderListItemDTO> records = result.getRecords().stream()
                .map(movieOrderViewConverter::toListItemView)
                .toList();
        return PageResult.of(result.getTotal(), records, result.getPage(), result.getSize());
    }
}