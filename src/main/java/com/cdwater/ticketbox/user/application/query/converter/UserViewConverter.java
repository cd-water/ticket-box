package com.cdwater.ticketbox.user.application.query.converter;

import com.cdwater.ticketbox.user.application.query.view.LoginView;
import com.cdwater.ticketbox.user.application.query.view.ProfileView;
import com.cdwater.ticketbox.user.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserViewConverter {

    ProfileView toProfileView(User user);

    @Mapping(target = "tokenName", source = "tokenName")
    @Mapping(target = "tokenValue", source = "tokenValue")
    LoginView toLoginView(User user, String tokenName, String tokenValue);
}
