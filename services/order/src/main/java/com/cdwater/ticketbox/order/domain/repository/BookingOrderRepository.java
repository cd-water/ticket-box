package com.cdwater.ticketbox.order.domain.repository;

import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.common.ticket.ProductType;
import com.cdwater.ticketbox.order.domain.model.BookingOrder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingOrderRepository {

    BookingOrder save(BookingOrder order);

    Optional<BookingOrder> findByOrderNo(Long orderNo);

    PageResult<BookingOrder> pageByUserAndProductType(Long userId, ProductType productType,
                                                      int status, long page, long size);

    boolean markPaid(Long orderNo, LocalDateTime payTime);

    boolean markCancelled(Long orderNo);

    boolean markCancelledFromPaid(Long orderNo);

    List<BookingOrder> findExpiredPending(LocalDateTime now, int limit);
}
