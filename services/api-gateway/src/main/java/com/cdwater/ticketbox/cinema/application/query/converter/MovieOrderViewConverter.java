package com.cdwater.ticketbox.cinema.application.query.converter;

import com.cdwater.ticketbox.cinema.application.query.view.MovieOrderDetailView;
import com.cdwater.ticketbox.cinema.application.query.view.MovieOrderListItemView;
import com.cdwater.ticketbox.cinema.application.query.view.MovieOrderSeatView;
import com.cdwater.ticketbox.cinema.domain.model.MovieOrder;
import com.cdwater.ticketbox.cinema.domain.model.MovieOrderSeat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MovieOrderViewConverter {

    @Mapping(target = "orderNo", source = "orderNo", qualifiedByName = "orderNoToString")
    @Mapping(target = "seats", ignore = true)
    MovieOrderDetailView toDetailView(MovieOrder order);

    @Mapping(target = "orderNo", source = "orderNo", qualifiedByName = "orderNoToString")
    MovieOrderListItemView toListItemView(MovieOrder order);

    List<MovieOrderSeatView> toSeatViews(List<MovieOrderSeat> seats);

    MovieOrderSeatView toSeatView(MovieOrderSeat seat);

    @Named("orderNoToString")
    static String orderNoToString(Long orderNo) {
        return String.valueOf(orderNo);
    }
}
