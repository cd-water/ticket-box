package com.cdwater.ticketbox.movie.adapter.out.client.converter;

import com.cdwater.ticketbox.movie.application.port.UserInfo;
import com.cdwater.ticketbox.user.application.query.UserBrief;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MovieUserInfoConverter {

    UserInfo toInfo(UserBrief user);
}
