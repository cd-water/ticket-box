package com.cdwater.ticketbox.movie.adapter.out.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdwater.ticketbox.movie.adapter.out.persistence.converter.MovieConverter;
import com.cdwater.ticketbox.movie.adapter.out.persistence.converter.MovieProjectionConverter;
import com.cdwater.ticketbox.movie.adapter.out.persistence.mapper.MovieGenreMapper;
import com.cdwater.ticketbox.movie.adapter.out.persistence.mapper.MovieMapper;
import com.cdwater.ticketbox.movie.adapter.out.persistence.mapper.MoviePersonMapper;
import com.cdwater.ticketbox.movie.adapter.out.persistence.po.MoviePO;
import com.cdwater.ticketbox.movie.domain.model.Genre;
import com.cdwater.ticketbox.movie.domain.model.Movie;
import com.cdwater.ticketbox.movie.domain.model.MoviePerson;
import com.cdwater.ticketbox.movie.domain.repository.MovieRepository;
import com.cdwater.ticketbox.persistence.LikeUtils;
import com.cdwater.ticketbox.common.result.PageResult;
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
    private final MoviePersonMapper moviePersonMapper;
    private final MovieProjectionConverter movieProjectionConverter;
    private final MovieConverter movieConverter;

    @Override
    public PageResult<Movie> page(String tab, String keyword, long page, long size) {
        LocalDateTime now = LocalDateTime.now();
        LambdaQueryWrapper<MoviePO> wrapper = new LambdaQueryWrapper<MoviePO>()
                .eq(MoviePO::getStatus, Movie.STATUS_ON_SHELF);
        if ("upcoming".equals(tab)) {
            wrapper.gt(MoviePO::getReleaseDate, now)
                    .orderByAsc(MoviePO::getReleaseDate)
                    .orderByDesc(MoviePO::getId);
        } else {
            wrapper.le(MoviePO::getReleaseDate, now)
                    .orderByDesc(MoviePO::getReleaseDate)
                    .orderByDesc(MoviePO::getId);
        }
        String trimmed = keyword == null ? "" : keyword.trim();
        if (!trimmed.isEmpty()) {
            wrapper.like(MoviePO::getTitle, LikeUtils.escape(trimmed));
        }
        Page<MoviePO> result = movieMapper.selectPage(new Page<>(page, size), wrapper);
        return PageResult.of(result.getTotal(), movieConverter.toDomain(result.getRecords()),
                result.getCurrent(), result.getSize());
    }

    @Override
    public Optional<Movie> findById(Long id) {
        return Optional.ofNullable(movieMapper.selectById(id))
                .map(movieConverter::toDomain);
    }

    @Override
    public Map<Long, Movie> findByIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Map.of();
        }
        return movieConverter.toDomain(movieMapper.selectByIds(ids)).stream()
                .collect(Collectors.toMap(Movie::getId, movie -> movie));
    }

    @Override
    public List<Genre> findGenres(Long movieId) {
        return movieGenreMapper.selectGenresByMovieId(movieId);
    }

    @Override
    public List<MoviePerson> findPersons(Long movieId) {
        return moviePersonMapper.selectPersonRowsByMovieId(movieId).stream()
                .map(movieProjectionConverter::toDomain)
                .toList();
    }
}
