package com.cdwater.ticketbox.content.play.application.query.converter;

import com.cdwater.ticketbox.content.api.dto.PlayDetailDTO;
import com.cdwater.ticketbox.content.api.dto.PlayListItemDTO;
import com.cdwater.ticketbox.content.play.domain.model.PlayItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PlayViewConverter {

    @Mapping(target = "stock", ignore = true)
    PlayListItemDTO toListItemView(PlayItem item);

    @Mapping(target = "description", ignore = true)
    @Mapping(target = "stock", ignore = true)
    PlayDetailDTO toDetailView(PlayItem item);
}