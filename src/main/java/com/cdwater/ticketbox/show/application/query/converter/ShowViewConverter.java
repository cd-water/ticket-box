package com.cdwater.ticketbox.show.application.query.converter;

import com.cdwater.ticketbox.show.application.query.view.ShowDetailView;
import com.cdwater.ticketbox.show.application.query.view.ShowListItemView;
import com.cdwater.ticketbox.show.application.query.view.TicketTypeView;
import com.cdwater.ticketbox.show.domain.model.Show;
import com.cdwater.ticketbox.show.domain.model.ShowTicketType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ShowViewConverter {

    @Mapping(target = "minPrice", ignore = true)
    ShowListItemView toListItemView(Show show);

    @Mapping(target = "ticketTypes", ignore = true)
    ShowDetailView toDetailView(Show show);

    @Mapping(target = "availableSeats", ignore = true)
    TicketTypeView toTicketTypeView(ShowTicketType type);
}
