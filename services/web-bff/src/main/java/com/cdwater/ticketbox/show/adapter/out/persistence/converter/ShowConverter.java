package com.cdwater.ticketbox.show.adapter.out.persistence.converter;

import com.cdwater.ticketbox.show.adapter.out.persistence.po.ShowPO;
import com.cdwater.ticketbox.show.domain.model.Show;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ShowConverter {

    Show toDomain(ShowPO po);

    List<Show> toDomain(List<ShowPO> poList);

    ShowPO toPO(Show domain);
}
