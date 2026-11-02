package com.cdwater.ticketbox.show.adapter.out.persistence.converter;

import com.cdwater.ticketbox.show.adapter.out.persistence.po.ShowOrderSeatPO;
import com.cdwater.ticketbox.show.domain.model.ShowOrderSeat;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ShowOrderSeatConverter {

    ShowOrderSeat toDomain(ShowOrderSeatPO po);

    List<ShowOrderSeat> toDomain(List<ShowOrderSeatPO> poList);

    ShowOrderSeatPO toPO(ShowOrderSeat domain);
}
