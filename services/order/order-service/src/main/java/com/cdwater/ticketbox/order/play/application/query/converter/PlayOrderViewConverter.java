package com.cdwater.ticketbox.order.play.application.query.converter;

import com.cdwater.ticketbox.order.api.dto.PlayOrderDetailDTO;
import com.cdwater.ticketbox.order.api.dto.PlayOrderListItemDTO;
import com.cdwater.ticketbox.order.play.domain.model.PlayOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PlayOrderViewConverter {

    @Mapping(target = "orderNo", source = "orderNo", qualifiedByName = "orderNoToString")
    PlayOrderDetailDTO toDetailView(PlayOrder order);

    @Mapping(target = "orderNo", source = "orderNo", qualifiedByName = "orderNoToString")
    PlayOrderListItemDTO toListItemView(PlayOrder order);

    @Named("orderNoToString")
    static String orderNoToString(Long orderNo) {
        return String.valueOf(orderNo);
    }
}