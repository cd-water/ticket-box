package com.cdwater.ticketbox.inventory.adapter.out.persistence.converter;

import com.cdwater.ticketbox.inventory.adapter.out.persistence.po.ShowSeatPO;
import com.cdwater.ticketbox.inventory.domain.model.ShowSeat;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ShowSeatConverter {

    ShowSeat toDomain(ShowSeatPO po);

    List<ShowSeat> toDomain(List<ShowSeatPO> poList);

    ShowSeatPO toPO(ShowSeat domain);
}
