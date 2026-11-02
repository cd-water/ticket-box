package com.cdwater.ticketbox.show.adapter.out.persistence.converter;

import com.cdwater.ticketbox.show.adapter.out.persistence.po.ShowTicketTypePO;
import com.cdwater.ticketbox.show.domain.model.ShowTicketType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ShowTicketTypeConverter {

    ShowTicketType toDomain(ShowTicketTypePO po);

    List<ShowTicketType> toDomain(List<ShowTicketTypePO> poList);

    ShowTicketTypePO toPO(ShowTicketType domain);
}
