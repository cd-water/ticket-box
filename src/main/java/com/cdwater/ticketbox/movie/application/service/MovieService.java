package com.cdwater.ticketbox.movie.application.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.common.result.ResultCode;
import com.cdwater.ticketbox.common.util.LikeUtils;
import com.cdwater.ticketbox.movie.infrastructure.persistence.projection.MovieCrewRow;
import com.cdwater.ticketbox.movie.api.dto.MovieListQuery;
import com.cdwater.ticketbox.movie.domain.model.Movie;
import com.cdwater.ticketbox.movie.domain.model.MovieComment;
import com.cdwater.ticketbox.movie.infrastructure.persistence.mapper.MovieCommentMapper;
import com.cdwater.ticketbox.movie.infrastructure.persistence.mapper.MovieCrewMapper;
import com.cdwater.ticketbox.movie.infrastructure.persistence.mapper.MovieGenreMapper;
import com.cdwater.ticketbox.movie.infrastructure.persistence.mapper.MovieMapper;
import com.cdwater.ticketbox.movie.api.vo.CrewVO;
import com.cdwater.ticketbox.movie.api.vo.GenreVO;
import com.cdwater.ticketbox.movie.api.vo.MovieDetailVO;
import com.cdwater.ticketbox.movie.api.vo.MovieListItemVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieMapper movieMapper;
    private final MovieGenreMapper movieGenreMapper;
    private final MovieCrewMapper movieCrewMapper;
    private final MovieCommentMapper movieCommentMapper;

    public PageResult<MovieListItemVO> list(MovieListQuery query) {
        LocalDateTime now = LocalDateTime.now();
        LambdaQueryWrapper<Movie> wrapper = new LambdaQueryWrapper<Movie>()
                .eq(Movie::getStatus, 1);
        if ("upcoming".equals(query.getTab())) {
            wrapper.gt(Movie::getReleaseDate, now)
                    .orderByAsc(Movie::getReleaseDate)
                    .orderByDesc(Movie::getId);
        } else {
            wrapper.le(Movie::getReleaseDate, now)
                    .orderByDesc(Movie::getReleaseDate)
                    .orderByDesc(Movie::getId);
        }
        String keyword = query.getKeyword() == null ? "" : query.getKeyword().trim();
        if (!keyword.isEmpty()) {
            wrapper.like(Movie::getTitle, LikeUtils.escape(keyword));
        }
        Page<Movie> page = movieMapper.selectPage(new Page<>(query.getPage(), query.getSize()), wrapper);
        List<MovieListItemVO> records = page.getRecords().stream().map(m -> {
            MovieListItemVO vo = new MovieListItemVO();
            vo.setId(m.getId());
            vo.setTitle(m.getTitle());
            vo.setPoster(m.getPoster());
            vo.setReleaseDate(m.getReleaseDate());
            return vo;
        }).toList();
        return PageResult.of(page.getTotal(), records, page.getCurrent(), page.getSize());
    }

    public MovieDetailVO detail(Long id) {
        Movie movie = movieMapper.selectById(id);
        if (movie == null || movie.getStatus() == null || movie.getStatus() != 1) {
            throw new BizException(ResultCode.NOT_FOUND, "电影不存在或已下架");
        }
        MovieDetailVO vo = new MovieDetailVO();
        vo.setId(movie.getId());
        vo.setTitle(movie.getTitle());
        vo.setPoster(movie.getPoster());
        vo.setSynopsis(movie.getSynopsis());
        vo.setDuration(movie.getDuration());
        vo.setReleaseDate(movie.getReleaseDate());
        vo.setTab(movie.getReleaseDate().isAfter(LocalDateTime.now()) ? "upcoming" : "showing");
        vo.setGenres(movieGenreMapper.selectGenresByMovieId(id).stream()
                .map(g -> new GenreVO(g.getId(), g.getName()))
                .toList());

        List<CrewVO> directors = new ArrayList<>();
        List<CrewVO> actors = new ArrayList<>();
        for (MovieCrewRow row : movieCrewMapper.selectCrewRowsByMovieId(id)) {
            CrewVO crew = new CrewVO(row.getPersonId(), row.getPersonName(),
                    row.getPersonAvatar(), row.getDuty() == 2 ? row.getRoleName() : null);
            if (row.getDuty() == 1) {
                directors.add(crew);
            } else if (row.getDuty() == 2) {
                actors.add(crew);
            }
        }
        vo.setDirectors(directors);
        vo.setActors(actors);
        vo.setReviewCount(movieCommentMapper.selectCount(new LambdaQueryWrapper<MovieComment>()
                .eq(MovieComment::getMovieId, id)
                .eq(MovieComment::getStatus, 1)));
        return vo;
    }
}
