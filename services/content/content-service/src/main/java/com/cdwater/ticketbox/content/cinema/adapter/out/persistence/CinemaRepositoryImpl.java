package com.cdwater.ticketbox.content.cinema.adapter.out.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdwater.ticketbox.content.cinema.adapter.out.persistence.converter.CinemaConverter;
import com.cdwater.ticketbox.content.cinema.adapter.out.persistence.po.CinemaPO;
import com.cdwater.ticketbox.content.cinema.domain.model.Cinema;
import com.cdwater.ticketbox.content.cinema.domain.repository.CinemaRepository;
import com.cdwater.ticketbox.content.cinema.adapter.out.persistence.mapper.CinemaMapper;
import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.content.movie.application.support.LikeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CinemaRepositoryImpl implements CinemaRepository {

    private final CinemaMapper cinemaMapper;
    private final CinemaConverter cinemaConverter;

    @Override
    public PageResult<Cinema> page(String name, Long movieId, long page, long size) {
        LambdaQueryWrapper<CinemaPO> wrapper = new LambdaQueryWrapper<CinemaPO>()
                .eq(CinemaPO::getStatus, 1);
        if (movieId != null) {
            wrapper.exists("""
                    SELECT 1 FROM t_screening s
                    WHERE s.cinema_id = t_cinema.id
                      AND s.movie_id = {0}
                      AND s.status = 1
                      AND s.start_time >= NOW()
                    """, movieId);
        }
        wrapper.orderByDesc(CinemaPO::getId);
        if (StringUtils.hasText(name)) {
            wrapper.like(CinemaPO::getName, LikeUtils.escape(name.trim()));
        }
        Page<CinemaPO> result = cinemaMapper.selectPage(new Page<>(page, size), wrapper);
        return PageResult.of(result.getTotal(), cinemaConverter.toDomain(result.getRecords()), result.getCurrent(), result.getSize());
    }

    @Override
    public Optional<Cinema> findById(Long id) {
        return Optional.ofNullable(cinemaMapper.selectById(id)).map(cinemaConverter::toDomain);
    }
}
