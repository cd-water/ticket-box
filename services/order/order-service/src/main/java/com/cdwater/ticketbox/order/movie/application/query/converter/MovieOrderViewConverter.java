package com.cdwater.ticketbox.order.movie.application.query.converter;

import com.cdwater.ticketbox.order.api.dto.MovieOrderDetailDTO;
import com.cdwater.ticketbox.order.api.dto.MovieOrderListItemDTO;
import com.cdwater.ticketbox.order.api.dto.MovieOrderSeatDTO;
import com.cdwater.ticketbox.order.movie.domain.model.MovieOrder;
import com.cdwater.ticketbox.order.movie.domain.model.MovieOrderSeat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MovieOrderViewConverter {

    @Mapping(target = "orderNo", source = "orderNo", qualifiedByName = "orderNoToString")
    @Mapping(target = "seats", ignore = true)
    MovieOrderDetailDTO toDetailView(MovieOrder order);

    @Mapping(target = "orderNo", source = "orderNo", qualifiedByName = "orderNoToString")
    MovieOrderListItemDTO toListItemView(MovieOrder order);

    List<MovieOrderSeatDTO> toSeatViews(List<MovieOrderSeat> seats);

    MovieOrderSeatDTO toSeatView(MovieOrderSeat seat);

    @Named("orderNoToString")
    static String orderNoToString(Long orderNo) {
        return String.valueOf(orderNo);
    }
}