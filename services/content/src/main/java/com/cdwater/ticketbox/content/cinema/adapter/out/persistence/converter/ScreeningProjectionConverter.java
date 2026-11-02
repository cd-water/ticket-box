package com.cdwater.ticketbox.content.cinema.adapter.out.persistence.converter;

import com.cdwater.ticketbox.content.cinema.adapter.out.persistence.projection.ScreeningRow;
import com.cdwater.ticketbox.content.cinema.domain.model.ScreeningDetails;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ScreeningProjectionConverter {

    ScreeningDetails toDomain(ScreeningRow row);
}
