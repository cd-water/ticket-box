package com.cdwater.ticketbox.order.show.adapter.out.persistence.converter;

import com.cdwater.ticketbox.order.show.adapter.out.persistence.po.ShowOrderPO;
import com.cdwater.ticketbox.order.show.domain.model.ShowOrder;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ShowOrderConverter {

    ShowOrder toDomain(ShowOrderPO po);

    List<ShowOrder> toDomain(List<ShowOrderPO> poList);

    ShowOrderPO toPO(ShowOrder domain);
}
