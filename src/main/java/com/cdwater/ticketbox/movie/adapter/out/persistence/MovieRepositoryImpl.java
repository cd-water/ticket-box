package com.cdwater.ticketbox.movie.adapter.out.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdwater.ticketbox.shared.query.PageResult;
import com.cdwater.ticketbox.shared.persistence.LikeUtils;
import com.cdwater.ticketbox.movie.adapter.out.persistence.mapper.MovieCrewMapper;
import com.cdwater.ticketbox.movie.adapter.out.persistence.mapper.MovieGenreMapper;
import com.cdwater.ticketbox.movie.adapter.out.persistence.mapper.MovieMapper;
import com.cdwater.ticketbox.movie.domain.model.Genre;
import com.cdwater.ticketbox.movie.domain.model.Movie;
import com.cdwater.ticketbox.movie.domain.model.MovieCrewMember;
import com.cdwater.ticketbox.movie.domain.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MovieRepositoryImpl implements MovieRepository {

    private final MovieMapper movieMapper;
    private final MovieGenreMapper movieGenreMapper;
    private final MovieCrewMapper movieCrewMapper;
    private final MovieProjectionMapper movieProjectionMapper;

    @Override
    public PageResult<Movie> page(String tab, String keyword, long page, long size) {
        LocalDateTime now = LocalDateTime.now();
        LambdaQueryWrapper<Movie> wrapper = new LambdaQueryWrapper<Movie>()
                .eq(Movie::getStatus, Movie.STATUS_ON_SHELF);
        if ("upcoming".equals(tab)) {
            wrapper.gt(Movie::getReleaseDate, now)
                    .orderByAsc(Movie::getReleaseDate)
                    .orderByDesc(Movie::getId);
        } else {
            wrapper.le(Movie::getReleaseDate, now)
                    .orderByDesc(Movie::getReleaseDate)
                    .orderByDesc(Movie::getId);
        }
        String trimmed = keyword == null ? "" : keyword.trim();
        if (!trimmed.isEmpty()) {
            wrapper.like(Movie::getTitle, LikeUtils.escape(trimmed));
        }
        Page<Movie> result = movieMapper.selectPage(new Page<>(page, size), wrapper);
        return PageResult.of(result.getTotal(), result.getRecords(),
                result.getCurrent(), result.getSize());
    }

    @Override
    public Optional<Movie> findById(Long id) {
        return Optional.ofNullable(movieMapper.selectById(id));
    }

    @Override
    public Map<Long, Movie> findByIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Map.of();
        }
        return movieMapper.selectByIds(ids).stream()
                .collect(Collectors.toMap(Movie::getId, movie -> movie));
    }

    @Override
    public List<Genre> findGenres(Long movieId) {
        return movieGenreMapper.selectGenresByMovieId(movieId);
    }

    @Override
    public List<MovieCrewMember> findCrew(Long movieId) {
        return movieCrewMapper.selectCrewRowsByMovieId(movieId).stream()
                .map(movieProjectionMapper::toDomain)
                .toList();
    }
}
