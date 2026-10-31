package com.cdwater.ticketbox.movie.application.query.mapper;

import com.cdwater.ticketbox.movie.application.port.UserInfo;
import com.cdwater.ticketbox.movie.application.query.view.ReplyView;
import com.cdwater.ticketbox.movie.application.query.view.ReviewUserView;
import com.cdwater.ticketbox.movie.application.query.view.ReviewView;
import com.cdwater.ticketbox.movie.domain.model.ReplyPreview;
import com.cdwater.ticketbox.movie.domain.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewViewMapper {

    ReviewUserView toUserView(UserInfo user);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "liked", ignore = true)
    @Mapping(target = "replies", ignore = true)
    ReviewView toBaseView(Review review);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "liked", ignore = true)
    @Mapping(target = "replyTo", ignore = true)
    ReplyView toReplyView(Review review);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "liked", ignore = true)
    @Mapping(target = "replyTo", ignore = true)
    ReplyView toReplyView(ReplyPreview preview);
}
