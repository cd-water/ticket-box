package com.cdwater.ticketbox.content.movie.application.query.support;

import com.cdwater.ticketbox.content.movie.application.port.UserInfo;
import com.cdwater.ticketbox.content.movie.application.query.converter.ReviewViewConverter;
import com.cdwater.ticketbox.content.api.dto.ReplyToDTO;
import com.cdwater.ticketbox.content.api.dto.ReplyDTO;
import com.cdwater.ticketbox.content.api.dto.ReviewUserDTO;
import com.cdwater.ticketbox.content.api.dto.ReviewDTO;
import com.cdwater.ticketbox.content.movie.domain.model.ReplyPreview;
import com.cdwater.ticketbox.content.movie.domain.model.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ReviewViewAssembler {

    private final ReviewViewConverter reviewViewMapper;

    public ReviewUserDTO toUserView(UserInfo user) {
        ReviewUserDTO view = reviewViewMapper.toUserView(user);
        return view == null ? new ReviewUserDTO(0L, "用户", "") : view;
    }

    public ReviewDTO baseReview(Review comment, UserInfo user, boolean liked) {
        ReviewDTO view = reviewViewMapper.toBaseView(comment);
        view.setUser(toUserView(user));
        view.setLiked(liked);
        return view;
    }

    public ReplyDTO toReplyView(ReplyPreview row,
                                 Map<Long, UserInfo> users,
                                 Map<Long, Review> parents,
                                 Set<Long> likedIds) {
        ReplyDTO view = reviewViewMapper.toReplyView(row);
        view.setUser(toUserView(users.get(row.getUserId())));
        view.setLiked(likedIds.contains(row.getId()));
        setReplyTo(view, row.getParentId(), users, parents);
        return view;
    }

    public ReplyDTO toReplyView(Review comment,
                                 Map<Long, UserInfo> users,
                                 Map<Long, Review> parents,
                                 Set<Long> likedIds) {
        ReplyDTO view = reviewViewMapper.toReplyView(comment);
        view.setUser(toUserView(users.get(comment.getUserId())));
        view.setLiked(likedIds.contains(comment.getId()));
        setReplyTo(view, comment.getParentId(), users, parents);
        return view;
    }

    private void setReplyTo(ReplyDTO view,
                            Long parentId,
                            Map<Long, UserInfo> users,
                            Map<Long, Review> parents) {
        Review parent = parents.get(parentId);
        if (parent == null) {
            return;
        }
        UserInfo parentUser = users.get(parent.getUserId());
        view.setReplyTo(new ReplyToDTO(parent.getId(),
                parentUser == null ? "用户" : parentUser.getNickname()));
    }
}