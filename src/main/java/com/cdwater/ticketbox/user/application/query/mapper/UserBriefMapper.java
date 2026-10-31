package com.cdwater.ticketbox.user.application.query.mapper;

import com.cdwater.ticketbox.user.application.query.UserBrief;
import com.cdwater.ticketbox.user.domain.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserBriefMapper {

    UserBrief toBrief(User user);
}
