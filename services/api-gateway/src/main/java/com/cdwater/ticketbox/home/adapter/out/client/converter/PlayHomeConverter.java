package com.cdwater.ticketbox.home.adapter.out.client.converter;

import com.cdwater.ticketbox.home.application.query.view.HomePlayItem;
import com.cdwater.ticketbox.play.application.query.view.PlayListItemView;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PlayHomeConverter {

    HomePlayItem toItem(PlayListItemView view);
}
