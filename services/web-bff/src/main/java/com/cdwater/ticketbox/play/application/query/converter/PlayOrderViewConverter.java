package com.cdwater.ticketbox.play.application.query.converter;

import com.cdwater.ticketbox.play.application.query.view.PlayOrderDetailView;
import com.cdwater.ticketbox.play.application.query.view.PlayOrderListItemView;
import com.cdwater.ticketbox.play.domain.model.PlayOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PlayOrderViewConverter {

    @Mapping(target = "orderNo", source = "orderNo", qualifiedByName = "orderNoToString")
    PlayOrderDetailView toDetailView(PlayOrder order);

    @Mapping(target = "orderNo", source = "orderNo", qualifiedByName = "orderNoToString")
    PlayOrderListItemView toListItemView(PlayOrder order);

    @Named("orderNoToString")
    static String orderNoToString(Long orderNo) {
        return String.valueOf(orderNo);
    }
}
