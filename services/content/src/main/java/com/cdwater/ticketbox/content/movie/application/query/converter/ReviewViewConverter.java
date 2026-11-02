package com.cdwater.ticketbox.content.movie.application.query.converter;

import com.cdwater.ticketbox.content.movie.application.port.UserInfo;
import com.cdwater.ticketbox.content.api.dto.ReplyDTO;
import com.cdwater.ticketbox.content.api.dto.ReviewUserDTO;
import com.cdwater.ticketbox.content.api.dto.ReviewDTO;
import com.cdwater.ticketbox.content.movie.domain.model.ReplyPreview;
import com.cdwater.ticketbox.content.movie.domain.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ReviewViewConverter {

    ReviewUserDTO toUserView(UserInfo user);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "liked", ignore = true)
    @Mapping(target = "replies", ignore = true)
    ReviewDTO toBaseView(Review review);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "liked", ignore = true)
    @Mapping(target = "replyTo", ignore = true)
    ReplyDTO toReplyView(Review review);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "liked", ignore = true)
    @Mapping(target = "replyTo", ignore = true)
    ReplyDTO toReplyView(ReplyPreview preview);
}