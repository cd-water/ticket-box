package com.cdwater.ticketbox.content.api;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.ticket.ProductType;
import com.cdwater.ticketbox.content.api.dto.BookingSnapshotDTO;

/**
 * 下单所需的商品快照：订单只依赖这一个读契约，不感知电影/演出/玩乐的内部模型。
 */
public interface CatalogApi {

    BookingSnapshotDTO getBookingSnapshot(ProductType productType, long productId, Long ticketTypeId)
            throws BizException;
}
