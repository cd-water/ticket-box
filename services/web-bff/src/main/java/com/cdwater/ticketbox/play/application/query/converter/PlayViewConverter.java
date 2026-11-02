package com.cdwater.ticketbox.play.application.query.converter;

import com.cdwater.ticketbox.play.application.query.view.PlayDetailView;
import com.cdwater.ticketbox.play.application.query.view.PlayListItemView;
import com.cdwater.ticketbox.play.domain.model.PlayItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PlayViewConverter {

    PlayListItemView toListItemView(PlayItem item);

    @Mapping(target = "description", ignore = true)
    PlayDetailView toDetailView(PlayItem item);
}
