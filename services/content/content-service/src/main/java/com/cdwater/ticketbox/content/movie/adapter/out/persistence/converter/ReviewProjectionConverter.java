package com.cdwater.ticketbox.content.movie.adapter.out.persistence.converter;

import com.cdwater.ticketbox.content.movie.adapter.out.persistence.projection.ReplyPreviewRow;
import com.cdwater.ticketbox.content.movie.domain.model.ReplyPreview;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ReviewProjectionConverter {

    ReplyPreview toDomain(ReplyPreviewRow row);
}
