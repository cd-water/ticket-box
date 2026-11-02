package com.cdwater.ticketbox.show.application.query.converter;

import com.cdwater.ticketbox.show.application.query.view.ShowOrderDetailView;
import com.cdwater.ticketbox.show.application.query.view.ShowOrderListItemView;
import com.cdwater.ticketbox.show.application.query.view.ShowOrderSeatView;
import com.cdwater.ticketbox.show.domain.model.ShowOrder;
import com.cdwater.ticketbox.show.domain.model.ShowOrderSeat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ShowOrderViewConverter {

    @Mapping(target = "orderNo", source = "orderNo", qualifiedByName = "orderNoToString")
    @Mapping(target = "seats", ignore = true)
    ShowOrderDetailView toDetailView(ShowOrder order);

    @Mapping(target = "orderNo", source = "orderNo", qualifiedByName = "orderNoToString")
    ShowOrderListItemView toListItemView(ShowOrder order);

    List<ShowOrderSeatView> toSeatViews(List<ShowOrderSeat> seats);

    @Named("orderNoToString")
    static String orderNoToString(Long orderNo) {
        return String.valueOf(orderNo);
    }
}
