package com.cdwater.ticketbox.order.movie.adapter.out.persistence.converter;

import com.cdwater.ticketbox.order.movie.adapter.out.persistence.po.MovieOrderPO;
import com.cdwater.ticketbox.order.movie.domain.model.MovieOrder;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MovieOrderConverter {

    MovieOrder toDomain(MovieOrderPO po);

    List<MovieOrder> toDomain(List<MovieOrderPO> poList);

    MovieOrderPO toPO(MovieOrder domain);
}
