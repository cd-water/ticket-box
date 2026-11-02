package com.cdwater.ticketbox.user.application.query.converter;

import com.cdwater.ticketbox.user.application.query.UserBrief;
import com.cdwater.ticketbox.user.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserBriefConverter {

    UserBrief toBrief(User user);
}
