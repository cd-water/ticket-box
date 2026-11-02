package com.cdwater.ticketbox.home.adapter.out.persistence.converter;

import com.cdwater.ticketbox.home.adapter.out.persistence.po.BannerPO;
import com.cdwater.ticketbox.home.domain.model.Banner;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface BannerConverter {

    Banner toDomain(BannerPO po);

    List<Banner> toDomain(List<BannerPO> poList);

    BannerPO toPO(Banner domain);
}
