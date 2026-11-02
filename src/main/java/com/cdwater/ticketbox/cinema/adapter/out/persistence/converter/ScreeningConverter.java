package com.cdwater.ticketbox.cinema.adapter.out.persistence.converter;

import com.cdwater.ticketbox.cinema.adapter.out.persistence.po.ScreeningPO;
import com.cdwater.ticketbox.cinema.domain.model.Screening;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ScreeningConverter {

    Screening toDomain(ScreeningPO po);

    ScreeningPO toPO(Screening domain);
}
