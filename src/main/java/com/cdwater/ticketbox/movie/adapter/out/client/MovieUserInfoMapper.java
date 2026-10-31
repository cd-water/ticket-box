package com.cdwater.ticketbox.movie.adapter.out.client;

import com.cdwater.ticketbox.movie.application.port.UserInfo;
import com.cdwater.ticketbox.user.application.query.UserBrief;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MovieUserInfoMapper {

    UserInfo toInfo(UserBrief user);
}
