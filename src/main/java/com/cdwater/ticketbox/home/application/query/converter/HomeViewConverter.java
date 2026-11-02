package com.cdwater.ticketbox.home.application.query.converter;

import com.cdwater.ticketbox.home.application.query.view.BannerView;
import com.cdwater.ticketbox.home.domain.model.Banner;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface HomeViewConverter {

    List<BannerView> toBannerViews(List<Banner> banners);
}
