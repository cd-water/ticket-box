package com.cdwater.ticketbox.cinema.adapter.out.persistence.converter;

import com.cdwater.ticketbox.cinema.adapter.out.persistence.po.CinemaPO;
import com.cdwater.ticketbox.cinema.domain.model.Cinema;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CinemaConverter {

    Cinema toDomain(CinemaPO po);

    List<Cinema> toDomain(List<CinemaPO> poList);

    CinemaPO toPO(Cinema domain);
}
