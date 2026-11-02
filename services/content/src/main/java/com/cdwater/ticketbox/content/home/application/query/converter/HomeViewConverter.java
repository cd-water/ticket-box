package com.cdwater.ticketbox.content.home.application.query.converter;

import com.cdwater.ticketbox.content.api.dto.BannerDTO;
import com.cdwater.ticketbox.content.home.domain.model.Banner;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface HomeViewConverter {

    List<BannerDTO> toBannerViews(List<Banner> banners);
}