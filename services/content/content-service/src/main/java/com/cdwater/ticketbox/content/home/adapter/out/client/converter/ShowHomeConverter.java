package com.cdwater.ticketbox.content.home.adapter.out.client.converter;

import com.cdwater.ticketbox.content.api.dto.HomeShowDTO;
import com.cdwater.ticketbox.content.api.dto.ShowListItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ShowHomeConverter {

    @Mapping(target = "minPrice", ignore = true)
    HomeShowDTO toItem(ShowListItemDTO view);
}