package com.cdwater.ticketbox.admin.adapter.out.persistence.converter;

import com.cdwater.ticketbox.admin.adapter.out.persistence.po.AdminPO;
import com.cdwater.ticketbox.admin.domain.model.Admin;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AdminConverter {

    Admin toDomain(AdminPO po);

    AdminPO toPO(Admin domain);
}
