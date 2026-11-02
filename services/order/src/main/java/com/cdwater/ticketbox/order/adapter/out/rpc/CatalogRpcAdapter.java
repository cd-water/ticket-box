package com.cdwater.ticketbox.order.adapter.out.rpc;

import com.cdwater.ticketbox.common.ticket.ProductType;
import com.cdwater.ticketbox.content.api.CatalogApi;
import com.cdwater.ticketbox.content.api.dto.BookingSnapshotDTO;
import com.cdwater.ticketbox.order.application.port.CatalogPort;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CatalogRpcAdapter implements CatalogPort {

    @DubboReference(check = false, timeout = 3000)
    private CatalogApi catalogApi;

    @Override
    public BookingSnapshotDTO getBookingSnapshot(ProductType productType, long productId, Long ticketTypeId) {
        return catalogApi.getBookingSnapshot(productType, productId, ticketTypeId);
    }
}
