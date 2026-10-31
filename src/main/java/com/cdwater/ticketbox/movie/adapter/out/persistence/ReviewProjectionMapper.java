package com.cdwater.ticketbox.movie.adapter.out.persistence;

import com.cdwater.ticketbox.movie.adapter.out.persistence.projection.ReplyPreviewRow;
import com.cdwater.ticketbox.movie.domain.model.ReplyPreview;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewProjectionMapper {

    ReplyPreview toDomain(ReplyPreviewRow row);
}
