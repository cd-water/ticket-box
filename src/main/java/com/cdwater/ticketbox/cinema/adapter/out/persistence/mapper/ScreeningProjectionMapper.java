package com.cdwater.ticketbox.cinema.adapter.out.persistence.mapper;

import com.cdwater.ticketbox.cinema.adapter.out.persistence.projection.ScreeningRow;
import com.cdwater.ticketbox.cinema.domain.model.ScreeningDetails;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScreeningProjectionMapper {

    ScreeningDetails toDomain(ScreeningRow row);
}
