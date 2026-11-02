package com.cdwater.ticketbox.content.home.adapter.out.client.converter;

import com.cdwater.ticketbox.content.api.dto.HomePlayDTO;
import com.cdwater.ticketbox.content.api.dto.PlayListItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PlayHomeConverter {

    HomePlayDTO toItem(PlayListItemDTO view);
}