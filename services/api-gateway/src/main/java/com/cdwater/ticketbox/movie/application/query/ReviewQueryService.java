package com.cdwater.ticketbox.movie.application.query;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.CursorResult;
import com.cdwater.ticketbox.common.result.ResultCode;
import com.cdwater.ticketbox.movie.application.query.support.ReviewViewAssembler;
import com.cdwater.ticketbox.movie.application.query.view.ReplyView;
import com.cdwater.ticketbox.movie.application.query.view.ReviewView;
import com.cdwater.ticketbox.movie.application.support.ReviewCursor;
import com.cdwater.ticketbox.movie.domain.model.Movie;
import com.cdwater.ticketbox.movie.domain.model.Review;
import com.cdwater.ticketbox.movie.domain.model.ReplyPreview;
import com.cdwater.ticketbox.movie.domain.repository.MovieRepository;
import com.cdwater.ticketbox.movie.domain.repository.ReviewRepository;
import com.cdwater.ticketbox.movie.application.port.UserInfo;
import com.cdwater.ticketbox.movie.application.port.UserInfoPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewQueryService {

    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;
    private final UserInfoPort userInfoPort;
    private final ReviewViewAssembler assembler;

    public CursorResult<ReviewView> listReviews(Long movieId, Long userId, ReviewListQuery query) {
        requireMovie(movieId);
        int size = (int) query.getSize();
        ReviewCursor.Cursor cursor = isEmpty(query.getCursor()) ? null : ReviewCursor.decode(query.getCursor());

        List<Review> fetched;
        if ("hot".equals(query.getSort())) {
            Integer likeCount = cursor == null ? null : ReviewCursor.parseInt(cursor.getValue());
            fetched = reviewRepository.findRootHot(movieId, likeCount, idOf(cursor), size + 1);
        } else {
            LocalDateTime time = cursor == null ? null : ReviewCursor.parseTime(cursor.getValue());
            fetched = reviewRepository.findRootLatest(movieId, time, idOf(cursor), size + 1);
        }
        boolean hasMore = fetched.size() > size;
        List<Review> roots = hasMore ? new ArrayList<>(fetched.subList(0, size)) : fetched;
        String nextCursor = hasMore ? encodeNext(query.getSort(), roots.getLast()) : null;

        List<Long> rootIds = roots.stream().map(Review::getId).toList();
        List<ReplyPreview> previews = rootIds.isEmpty()
                ? List.of()
                : reviewRepository.findReplyPreviews(rootIds);
        Map<Long, List<ReplyPreview>> previewsByRoot = previews.stream()
                .collect(Collectors.groupingBy(ReplyPreview::getRootId));

        Set<Long> parentIds = previews.stream()
                .map(ReplyPreview::getParentId)
                .filter(pid -> pid != 0 && !rootIds.contains(pid))
                .collect(Collectors.toSet());
        Map<Long, Review> parents = new HashMap<>();
        roots.forEach(root -> parents.put(root.getId(), root));
        if (!parentIds.isEmpty()) {
            reviewRepository.findByIds(parentIds).forEach(c -> parents.put(c.getId(), c));
        }

        Set<Long> userIds = new HashSet<>();
        roots.forEach(root -> userIds.add(root.getUserId()));
        previews.forEach(p -> userIds.add(p.getUserId()));
        parents.values().forEach(p -> userIds.add(p.getUserId()));
        Map<Long, UserInfo> users = loadUsers(userIds);

        Set<Long> commentIds = new HashSet<>(rootIds);
        previews.forEach(p -> commentIds.add(p.getId()));
        Set<Long> likedIds = likedIds(userId, commentIds);

        List<ReviewView> records = roots.stream().map(root -> {
            ReviewView view = assembler.baseReview(root, users.get(root.getUserId()),
                    likedIds.contains(root.getId()));
            view.setReplies(previewsByRoot.getOrDefault(root.getId(), List.of()).stream()
                    .map(p -> assembler.toReplyView(p, users, parents, likedIds))
                    .toList());
            return view;
        }).toList();
        return CursorResult.of(records, nextCursor, hasMore);
    }

    public CursorResult<ReplyView> listReplies(Long rootId, Long userId, ReplyListQuery query) {
        Review root = reviewRepository.findById(rootId).orElse(null);
        if (root == null || !root.isVisible() || !root.isRoot()) {
            throw new BizException(ResultCode.NOT_FOUND, "影评不存在或不可见");
        }
        int size = (int) query.getSize();
        ReviewCursor.Cursor cursor = isEmpty(query.getCursor()) ? null : ReviewCursor.decode(query.getCursor());
        LocalDateTime time = cursor == null ? null : ReviewCursor.parseTime(cursor.getValue());
        Long cursorId = idOf(cursor);

        List<Review> fetched = reviewRepository.findReplies(rootId, time, cursorId, size + 1);
        boolean hasMore = fetched.size() > size;
        List<Review> replies = hasMore ? new ArrayList<>(fetched.subList(0, size)) : fetched;
        String nextCursor = hasMore
                ? ReviewCursor.encode(replies.getLast().getCreateTime().toString(),
                replies.getLast().getId())
                : null;

        Set<Long> parentIds = replies.stream()
                .map(Review::getParentId)
                .filter(pid -> pid != 0 && !pid.equals(rootId))
                .collect(Collectors.toSet());
        Map<Long, Review> parents = new HashMap<>();
        parents.put(root.getId(), root);
        if (!parentIds.isEmpty()) {
            reviewRepository.findByIds(parentIds).forEach(c -> parents.put(c.getId(), c));
        }

        Set<Long> userIds = new HashSet<>();
        replies.forEach(r -> userIds.add(r.getUserId()));
        parents.values().forEach(p -> userIds.add(p.getUserId()));
        Map<Long, UserInfo> users = loadUsers(userIds);
        Set<Long> commentIds = replies.stream().map(Review::getId).collect(Collectors.toSet());
        Set<Long> likedIds = likedIds(userId, commentIds);

        List<ReplyView> records = replies.stream()
                .map(r -> assembler.toReplyView(r, users, parents, likedIds))
                .toList();
        return CursorResult.of(records, nextCursor, hasMore);
    }

    private void requireMovie(Long movieId) {
        movieRepository.findById(movieId)
                .filter(Movie::isOnShelf)
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "电影不存在或已下架"));
    }

    private Map<Long, UserInfo> loadUsers(Collection<Long> ids) {
        return userInfoPort.findByIds(ids);
    }

    private Set<Long> likedIds(Long userId, Collection<Long> commentIds) {
        return reviewRepository.findLikedCommentIds(userId, commentIds);
    }

    private String encodeNext(String sort, Review last) {
        if ("hot".equals(sort)) {
            return ReviewCursor.encode(String.valueOf(last.getLikeCount()), last.getId());
        }
        return ReviewCursor.encode(last.getCreateTime().toString(), last.getId());
    }

    private static Long idOf(ReviewCursor.Cursor cursor) {
        return cursor == null ? null : cursor.getId();
    }

    private static boolean isEmpty(String value) {
        return value == null || value.isBlank();
    }
}
