package com.cdwater.ticketbox.content.show.application.query.converter;

import com.cdwater.ticketbox.content.api.dto.ShowDetailDTO;
import com.cdwater.ticketbox.content.api.dto.ShowListItemDTO;
import com.cdwater.ticketbox.content.api.dto.TicketTypeDTO;
import com.cdwater.ticketbox.content.show.domain.model.Show;
import com.cdwater.ticketbox.content.show.domain.model.ShowTicketType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ShowViewConverter {

    @Mapping(target = "minPrice", ignore = true)
    ShowListItemDTO toListItemView(Show show);

    @Mapping(target = "ticketTypes", ignore = true)
    ShowDetailDTO toDetailView(Show show);

    @Mapping(target = "availableSeats", ignore = true)
    TicketTypeDTO toTicketTypeView(ShowTicketType type);
}