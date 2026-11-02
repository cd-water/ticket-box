package com.cdwater.ticketbox.order.application.port;

import com.cdwater.ticketbox.common.ticket.ProductType;
import com.cdwater.ticketbox.content.api.dto.BookingSnapshotDTO;

public interface CatalogPort {

    BookingSnapshotDTO getBookingSnapshot(ProductType productType, long productId, Long ticketTypeId);
}
