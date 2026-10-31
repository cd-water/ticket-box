package com.cdwater.ticketbox.movie.application.query.support;

import com.cdwater.ticketbox.movie.application.port.UserInfo;
import com.cdwater.ticketbox.movie.application.query.mapper.ReviewViewMapper;
import com.cdwater.ticketbox.movie.application.query.view.ReplyToView;
import com.cdwater.ticketbox.movie.application.query.view.ReplyView;
import com.cdwater.ticketbox.movie.application.query.view.ReviewUserView;
import com.cdwater.ticketbox.movie.application.query.view.ReviewView;
import com.cdwater.ticketbox.movie.domain.model.ReplyPreview;
import com.cdwater.ticketbox.movie.domain.model.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ReviewViewAssembler {

    private final ReviewViewMapper reviewViewMapper;

    public ReviewUserView toUserView(UserInfo user) {
        ReviewUserView view = reviewViewMapper.toUserView(user);
        return view == null ? new ReviewUserView(0L, "用户", "") : view;
    }

    public ReviewView baseReview(Review comment, UserInfo user, boolean liked) {
        ReviewView view = reviewViewMapper.toBaseView(comment);
        view.setUser(toUserView(user));
        view.setLiked(liked);
        return view;
    }

    public ReplyView toReplyView(ReplyPreview row,
                                 Map<Long, UserInfo> users,
                                 Map<Long, Review> parents,
                                 Set<Long> likedIds) {
        ReplyView view = reviewViewMapper.toReplyView(row);
        view.setUser(toUserView(users.get(row.getUserId())));
        view.setLiked(likedIds.contains(row.getId()));
        setReplyTo(view, row.getParentId(), users, parents);
        return view;
    }

    public ReplyView toReplyView(Review comment,
                                 Map<Long, UserInfo> users,
                                 Map<Long, Review> parents,
                                 Set<Long> likedIds) {
        ReplyView view = reviewViewMapper.toReplyView(comment);
        view.setUser(toUserView(users.get(comment.getUserId())));
        view.setLiked(likedIds.contains(comment.getId()));
        setReplyTo(view, comment.getParentId(), users, parents);
        return view;
    }

    private void setReplyTo(ReplyView view,
                            Long parentId,
                            Map<Long, UserInfo> users,
                            Map<Long, Review> parents) {
        Review parent = parents.get(parentId);
        if (parent == null) {
            return;
        }
        UserInfo parentUser = users.get(parent.getUserId());
        view.setReplyTo(new ReplyToView(parent.getId(),
                parentUser == null ? "用户" : parentUser.getNickname()));
    }
}
