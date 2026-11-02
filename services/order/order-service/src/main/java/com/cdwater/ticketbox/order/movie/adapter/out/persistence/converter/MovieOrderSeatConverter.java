package com.cdwater.ticketbox.order.movie.adapter.out.persistence.converter;

import com.cdwater.ticketbox.order.movie.adapter.out.persistence.po.MovieOrderSeatPO;
import com.cdwater.ticketbox.order.movie.domain.model.MovieOrderSeat;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MovieOrderSeatConverter {

    MovieOrderSeat toDomain(MovieOrderSeatPO po);

    List<MovieOrderSeat> toDomain(List<MovieOrderSeatPO> poList);

    MovieOrderSeatPO toPO(MovieOrderSeat domain);
}
