package com.cdwater.ticketbox.content.cinema.adapter.out.persistence.converter;

import com.cdwater.ticketbox.content.cinema.adapter.out.persistence.po.HallPO;
import com.cdwater.ticketbox.content.cinema.domain.model.Hall;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface HallConverter {

    Hall toDomain(HallPO po);

    HallPO toPO(Hall domain);
}
