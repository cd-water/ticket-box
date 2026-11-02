package com.cdwater.ticketbox.play.adapter.out.persistence.converter;

import com.cdwater.ticketbox.play.adapter.out.persistence.po.PlayItemPO;
import com.cdwater.ticketbox.play.domain.model.PlayItem;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PlayItemConverter {

    PlayItem toDomain(PlayItemPO po);

    List<PlayItem> toDomain(List<PlayItemPO> poList);

    PlayItemPO toPO(PlayItem domain);
}
