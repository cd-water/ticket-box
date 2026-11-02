package com.cdwater.ticketbox.movie.adapter.out.persistence.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cdwater.ticketbox.movie.adapter.out.persistence.projection.ReplyPreviewRow;
import com.cdwater.ticketbox.movie.domain.model.Genre;
import com.cdwater.ticketbox.movie.adapter.out.persistence.po.MoviePO;
import com.cdwater.ticketbox.movie.adapter.out.persistence.po.ReviewPO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieDomainMapperTest {

    @Autowired
    MovieMapper movieMapper;

    @Autowired
    MovieGenreMapper genreMapper;

    @Autowired
    MoviePersonMapper personMapper;

    @Autowired
    ReviewMapper commentMapper;

    @Autowired
    ReviewLikeMapper likeMapper;

    @Test
    void seed_movie_filters() {
        LocalDateTime now = LocalDateTime.now();
        Long hot = movieMapper.selectCount(new LambdaQueryWrapper<MoviePO>()
                .eq(MoviePO::getStatus, 1)
                .le(MoviePO::getReleaseDate, now));
        Long upcoming = movieMapper.selectCount(new LambdaQueryWrapper<MoviePO>()
                .eq(MoviePO::getStatus, 1)
                .gt(MoviePO::getReleaseDate, now));
        assertEquals(15L, hot);
        assertEquals(15L, upcoming);
    }

    @Test
    void movie1_genres_and_persons() {
        List<String> genres = genreMapper.selectGenresByMovieId(1L).stream()
                .map(Genre::getName).toList();
        assertTrue(genres.contains("科幻"));
        assertTrue(genres.contains("冒险"));

        var rows = personMapper.selectPersonRowsByMovieId(1L);
        assertTrue(rows.stream().anyMatch(r -> r.getDuty() == 1 && "张远航".equals(r.getPersonName())));
        assertTrue(rows.stream().anyMatch(r -> r.getDuty() == 2
                && "林晓".equals(r.getPersonName()) && "陆衡".equals(r.getRoleName())));
    }

    @Test
    void movie1_hot_reviews_and_previews() {
        List<ReviewPO> roots = commentMapper.selectRootHot(1L, null, null, 10);
        assertEquals(1L, roots.get(0).getId());
        assertEquals(8, roots.get(0).getLikeCount());

        List<ReplyPreviewRow> previews = commentMapper.selectReplyPreviews(List.of(1L));
        assertEquals(List.of(3L, 4L), previews.stream().map(ReplyPreviewRow::getId).toList());
    }

    @Test
    void seed_likes_are_queryable() {
        List<Long> liked = likeMapper.selectLikedCommentIds(1L, List.of(2L, 33L));
        assertEquals(2, liked.size());
        assertTrue(liked.containsAll(List.of(2L, 33L)));
    }
}
