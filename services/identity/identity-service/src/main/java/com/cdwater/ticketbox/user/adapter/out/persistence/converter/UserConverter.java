package com.cdwater.ticketbox.user.adapter.out.persistence.converter;

import com.cdwater.ticketbox.user.adapter.out.persistence.po.UserPO;
import com.cdwater.ticketbox.user.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserConverter {

    User toDomain(UserPO po);

    List<User> toDomain(List<UserPO> poList);

    UserPO toPO(User domain);
}
