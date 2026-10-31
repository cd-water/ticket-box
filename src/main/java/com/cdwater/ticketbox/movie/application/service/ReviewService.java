package com.cdwater.ticketbox.movie.application.service;

import com.cdwater.ticketbox.auth.domain.model.User;
import com.cdwater.ticketbox.auth.infrastructure.persistence.UserMapper;
import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.CursorResult;
import com.cdwater.ticketbox.common.result.ResultCode;
import com.cdwater.ticketbox.config.StpKit;
import com.cdwater.ticketbox.movie.api.dto.ReplyListQuery;
import com.cdwater.ticketbox.movie.infrastructure.persistence.projection.ReplyPreviewRow;
import com.cdwater.ticketbox.movie.api.dto.ReviewCreateDTO;
import com.cdwater.ticketbox.movie.api.dto.ReviewListQuery;
import com.cdwater.ticketbox.movie.application.support.ReviewCursor;
import com.cdwater.ticketbox.movie.domain.model.Movie;
import com.cdwater.ticketbox.movie.domain.model.MovieComment;
import com.cdwater.ticketbox.movie.infrastructure.persistence.mapper.MovieCommentLikeMapper;
import com.cdwater.ticketbox.movie.infrastructure.persistence.mapper.MovieCommentMapper;
import com.cdwater.ticketbox.movie.infrastructure.persistence.mapper.MovieMapper;
import com.cdwater.ticketbox.movie.api.vo.LikeResultVO;
import com.cdwater.ticketbox.movie.api.vo.ReplyToVO;
import com.cdwater.ticketbox.movie.api.vo.ReplyVO;
import com.cdwater.ticketbox.movie.api.vo.ReviewUserVO;
import com.cdwater.ticketbox.movie.api.vo.ReviewVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class ReviewService {

    private final MovieMapper movieMapper;
    private final MovieCommentMapper commentMapper;
    private final MovieCommentLikeMapper likeMapper;
    private final UserMapper userMapper;

    public CursorResult<ReviewVO> listReviews(Long movieId, ReviewListQuery query) {
        requireMovie(movieId);
        Long userId = StpKit.USER.getLoginIdAsLong();
        int size = (int) query.getSize();
        ReviewCursor.Cursor cursor = isEmpty(query.getCursor()) ? null : ReviewCursor.decode(query.getCursor());

        List<MovieComment> fetched;
        if ("hot".equals(query.getSort())) {
            Integer likeCount = cursor == null ? null : ReviewCursor.parseInt(cursor.value());
            fetched = commentMapper.selectRootHot(movieId, likeCount, idOf(cursor), size + 1);
        } else {
            LocalDateTime time = cursor == null ? null : ReviewCursor.parseTime(cursor.value());
            fetched = commentMapper.selectRootLatest(movieId, time, idOf(cursor), size + 1);
        }
        boolean hasMore = fetched.size() > size;
        List<MovieComment> roots = hasMore ? new ArrayList<>(fetched.subList(0, size)) : fetched;
        String nextCursor = hasMore ? encodeNext(query.getSort(), roots.getLast()) : null;

        List<Long> rootIds = roots.stream().map(MovieComment::getId).toList();
        List<ReplyPreviewRow> previews = rootIds.isEmpty()
                ? List.of()
                : commentMapper.selectReplyPreviews(rootIds);
        Map<Long, List<ReplyPreviewRow>> previewsByRoot = previews.stream()
                .collect(Collectors.groupingBy(ReplyPreviewRow::getRootId));

        Set<Long> parentIds = previews.stream()
                .map(ReplyPreviewRow::getParentId)
                .filter(pid -> pid != 0 && !rootIds.contains(pid))
                .collect(Collectors.toSet());
        Map<Long, MovieComment> parents = new HashMap<>();
        roots.forEach(root -> parents.put(root.getId(), root));
        if (!parentIds.isEmpty()) {
            commentMapper.selectByIds(parentIds).forEach(c -> parents.put(c.getId(), c));
        }

        Set<Long> userIds = new HashSet<>();
        roots.forEach(root -> userIds.add(root.getUserId()));
        previews.forEach(p -> userIds.add(p.getUserId()));
        parents.values().forEach(p -> userIds.add(p.getUserId()));
        Map<Long, User> users = loadUsers(userIds);

        Set<Long> commentIds = new HashSet<>(rootIds);
        previews.forEach(p -> commentIds.add(p.getId()));
        Set<Long> likedIds = likedIds(userId, commentIds);

        List<ReviewVO> records = roots.stream().map(root -> {
            ReviewVO vo = baseReview(root, users.get(root.getUserId()), likedIds.contains(root.getId()));
            vo.setReplies(previewsByRoot.getOrDefault(root.getId(), List.of()).stream()
                    .map(p -> toReplyVO(p, users, parents, likedIds))
                    .toList());
            return vo;
        }).toList();
        return CursorResult.of(records, nextCursor, hasMore);
    }

    public CursorResult<ReplyVO> listReplies(Long rootId, ReplyListQuery query) {
        MovieComment root = commentMapper.selectById(rootId);
        if (root == null || root.getStatus() == null || root.getStatus() != 1 || root.getRootId() != 0) {
            throw new BizException(ResultCode.NOT_FOUND, "影评不存在或不可见");
        }
        Long userId = StpKit.USER.getLoginIdAsLong();
        int size = (int) query.getSize();
        ReviewCursor.Cursor cursor = isEmpty(query.getCursor()) ? null : ReviewCursor.decode(query.getCursor());
        LocalDateTime time = cursor == null ? null : ReviewCursor.parseTime(cursor.value());
        Long cursorId = idOf(cursor);

        List<MovieComment> fetched = commentMapper.selectReplies(rootId, time, cursorId, size + 1);
        boolean hasMore = fetched.size() > size;
        List<MovieComment> replies = hasMore ? new ArrayList<>(fetched.subList(0, size)) : fetched;
        String nextCursor = hasMore
                ? ReviewCursor.encode(replies.getLast().getCreateTime().toString(),
                replies.getLast().getId())
                : null;

        Set<Long> parentIds = replies.stream()
                .map(MovieComment::getParentId)
                .filter(pid -> pid != 0 && !pid.equals(rootId))
                .collect(Collectors.toSet());
        Map<Long, MovieComment> parents = new HashMap<>();
        parents.put(root.getId(), root);
        if (!parentIds.isEmpty()) {
            commentMapper.selectByIds(parentIds).forEach(c -> parents.put(c.getId(), c));
        }

        Set<Long> userIds = new HashSet<>();
        replies.forEach(r -> userIds.add(r.getUserId()));
        parents.values().forEach(p -> userIds.add(p.getUserId()));
        Map<Long, User> users = loadUsers(userIds);
        Set<Long> commentIds = replies.stream().map(MovieComment::getId).collect(Collectors.toSet());
        Set<Long> likedIds = likedIds(userId, commentIds);

        List<ReplyVO> records = replies.stream()
                .map(r -> toReplyVO(r, users, parents, likedIds))
                .toList();
        return CursorResult.of(records, nextCursor, hasMore);
    }

    @Transactional
    public ReviewVO createReview(Long movieId, ReviewCreateDTO dto) {
        requireMovie(movieId);
        Long userId = StpKit.USER.getLoginIdAsLong();
        MovieComment comment = new MovieComment();
        comment.setMovieId(movieId);
        comment.setUserId(userId);
        comment.setRootId(0L);
        comment.setParentId(0L);
        comment.setContent(dto.getContent().trim());
        comment.setLikeCount(0);
        comment.setReplyCount(0);
        comment.setStatus(1);
        commentMapper.insert(comment);
        MovieComment saved = commentMapper.selectById(comment.getId());
        User user = userMapper.selectById(userId);
        return baseReview(saved, user, false);
    }

    @Transactional
    public ReplyVO reply(Long targetId, ReviewCreateDTO dto) {
        MovieComment target = commentMapper.selectById(targetId);
        if (target == null || target.getStatus() == null || target.getStatus() != 1) {
            throw new BizException(ResultCode.NOT_FOUND, "影评不存在或不可见");
        }
        requireMovie(target.getMovieId());
        Long userId = StpKit.USER.getLoginIdAsLong();
        Long rootId = target.getRootId() == 0 ? target.getId() : target.getRootId();
        MovieComment root = commentMapper.selectById(rootId);
        if (root == null || root.getStatus() == null || root.getStatus() != 1 || root.getRootId() != 0) {
            throw new BizException(ResultCode.NOT_FOUND, "影评不存在或不可见");
        }

        MovieComment reply = new MovieComment();
        reply.setMovieId(target.getMovieId());
        reply.setUserId(userId);
        reply.setRootId(rootId);
        reply.setParentId(target.getId());
        reply.setContent(dto.getContent().trim());
        reply.setLikeCount(0);
        reply.setReplyCount(0);
        reply.setStatus(1);
        commentMapper.insert(reply);
        commentMapper.incrementReplyCount(rootId);

        MovieComment saved = commentMapper.selectById(reply.getId());
        Set<Long> userIds = new HashSet<>();
        userIds.add(userId);
        userIds.add(target.getUserId());
        Map<Long, User> users = loadUsers(userIds);
        Map<Long, MovieComment> parents = Map.of(target.getId(), target);
        return toReplyVO(saved, users, parents, Set.of());
    }

    @Transactional
    public LikeResultVO toggleLike(Long commentId) {
        // 先锁父评论行，串行化同一评论的并发点赞，避免 INSERT IGNORE 唯一键共享锁升级死锁
        MovieComment comment = commentMapper.selectByIdForUpdate(commentId);
        if (comment == null || comment.getStatus() == null || comment.getStatus() != 1) {
            throw new BizException(ResultCode.NOT_FOUND, "影评不存在或不可见");
        }
        Long userId = StpKit.USER.getLoginIdAsLong();
        int inserted = likeMapper.insertIgnore(commentId, userId);
        boolean liked;
        if (inserted == 1) {
            commentMapper.incrementLikeCount(commentId);
            liked = true;
        } else {
            int deleted = likeMapper.deleteLike(commentId, userId);
            if (deleted == 1) {
                commentMapper.decrementLikeCount(commentId);
            }
            liked = false;
        }
        MovieComment updated = commentMapper.selectById(commentId);
        return new LikeResultVO(liked, updated.getLikeCount());
    }

    protected Movie requireMovie(Long movieId) {
        Movie movie = movieMapper.selectById(movieId);
        if (movie == null || movie.getStatus() == null || movie.getStatus() != 1) {
            throw new BizException(ResultCode.NOT_FOUND, "电影不存在或已下架");
        }
        return movie;
    }

    protected Map<Long, User> loadUsers(Collection<Long> ids) {
        if (ids.isEmpty()) {
            return Map.of();
        }
        return userMapper.selectByIds(ids).stream()
                .collect(Collectors.toMap(User::getId, u -> u));
    }

    protected Set<Long> likedIds(Long userId, Collection<Long> commentIds) {
        if (commentIds.isEmpty()) {
            return Set.of();
        }
        return new HashSet<>(likeMapper.selectLikedCommentIds(userId, new ArrayList<>(commentIds)));
    }

    protected ReviewUserVO toUserVO(User user) {
        if (user == null) {
            return new ReviewUserVO(0L, "用户", "");
        }
        return new ReviewUserVO(user.getId(), user.getNickname(), user.getAvatar());
    }

    protected ReviewVO baseReview(MovieComment comment, User user, boolean liked) {
        ReviewVO vo = new ReviewVO();
        vo.setId(comment.getId());
        vo.setContent(comment.getContent());
        vo.setCreateTime(comment.getCreateTime());
        vo.setUser(toUserVO(user));
        vo.setLikeCount(comment.getLikeCount());
        vo.setLiked(liked);
        vo.setReplyCount(comment.getReplyCount());
        return vo;
    }

    protected ReplyVO toReplyVO(ReplyPreviewRow row,
                                Map<Long, User> users,
                                Map<Long, MovieComment> parents,
                                Set<Long> likedIds) {
        ReplyVO vo = new ReplyVO();
        vo.setId(row.getId());
        vo.setRootId(row.getRootId());
        vo.setParentId(row.getParentId());
        vo.setContent(row.getContent());
        vo.setCreateTime(row.getCreateTime());
        vo.setUser(toUserVO(users.get(row.getUserId())));
        vo.setLikeCount(row.getLikeCount());
        vo.setLiked(likedIds.contains(row.getId()));
        setReplyTo(vo, row.getParentId(), users, parents);
        return vo;
    }

    protected ReplyVO toReplyVO(MovieComment comment,
                                Map<Long, User> users,
                                Map<Long, MovieComment> parents,
                                Set<Long> likedIds) {
        ReplyVO vo = new ReplyVO();
        vo.setId(comment.getId());
        vo.setRootId(comment.getRootId());
        vo.setParentId(comment.getParentId());
        vo.setContent(comment.getContent());
        vo.setCreateTime(comment.getCreateTime());
        vo.setUser(toUserVO(users.get(comment.getUserId())));
        vo.setLikeCount(comment.getLikeCount());
        vo.setLiked(likedIds.contains(comment.getId()));
        setReplyTo(vo, comment.getParentId(), users, parents);
        return vo;
    }

    private void setReplyTo(ReplyVO vo,
                            Long parentId,
                            Map<Long, User> users,
                            Map<Long, MovieComment> parents) {
        MovieComment parent = parents.get(parentId);
        if (parent == null) {
            return;
        }
        User parentUser = users.get(parent.getUserId());
        vo.setReplyTo(new ReplyToVO(parent.getId(),
                parentUser == null ? "用户" : parentUser.getNickname()));
    }

    private String encodeNext(String sort, MovieComment last) {
        if ("hot".equals(sort)) {
            return ReviewCursor.encode(String.valueOf(last.getLikeCount()), last.getId());
        }
        return ReviewCursor.encode(last.getCreateTime().toString(), last.getId());
    }

    private static Long idOf(ReviewCursor.Cursor cursor) {
        return cursor == null ? null : cursor.id();
    }

    private static boolean isEmpty(String value) {
        return value == null || value.isBlank();
    }
}
