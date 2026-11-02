package com.cdwater.ticketbox.order.show.application.query.converter;

import com.cdwater.ticketbox.order.api.dto.ShowOrderDetailDTO;
import com.cdwater.ticketbox.order.api.dto.ShowOrderListItemDTO;
import com.cdwater.ticketbox.order.api.dto.ShowOrderSeatDTO;
import com.cdwater.ticketbox.order.show.domain.model.ShowOrder;
import com.cdwater.ticketbox.order.show.domain.model.ShowOrderSeat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ShowOrderViewConverter {

    @Mapping(target = "orderNo", source = "orderNo", qualifiedByName = "orderNoToString")
    @Mapping(target = "seats", ignore = true)
    ShowOrderDetailDTO toDetailView(ShowOrder order);

    @Mapping(target = "orderNo", source = "orderNo", qualifiedByName = "orderNoToString")
    ShowOrderListItemDTO toListItemView(ShowOrder order);

    List<ShowOrderSeatDTO> toSeatViews(List<ShowOrderSeat> seats);

    @Named("orderNoToString")
    static String orderNoToString(Long orderNo) {
        return String.valueOf(orderNo);
    }
}