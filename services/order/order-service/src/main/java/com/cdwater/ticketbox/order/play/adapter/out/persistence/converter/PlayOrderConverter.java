package com.cdwater.ticketbox.order.play.adapter.out.persistence.converter;

import com.cdwater.ticketbox.order.play.adapter.out.persistence.po.PlayOrderPO;
import com.cdwater.ticketbox.order.play.domain.model.PlayOrder;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PlayOrderConverter {

    PlayOrder toDomain(PlayOrderPO po);

    List<PlayOrder> toDomain(List<PlayOrderPO> poList);

    PlayOrderPO toPO(PlayOrder domain);
}
