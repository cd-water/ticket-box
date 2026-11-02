package com.cdwater.ticketbox.home.adapter.out.client.converter;

import com.cdwater.ticketbox.home.application.query.view.HomeShowItem;
import com.cdwater.ticketbox.show.application.query.view.ShowListItemView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ShowHomeConverter {

    @Mapping(target = "minPrice", ignore = true)
    HomeShowItem toItem(ShowListItemView view);
}
