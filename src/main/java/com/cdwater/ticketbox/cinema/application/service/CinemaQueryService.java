package com.cdwater.ticketbox.cinema.application.service;

import com.cdwater.ticketbox.cinema.api.dto.CinemaListQuery;
import com.cdwater.ticketbox.cinema.api.vo.CinemaDetailVO;
import com.cdwater.ticketbox.cinema.api.vo.CinemaListItemVO;
import com.cdwater.ticketbox.cinema.api.vo.CinemaMovieVO;
import com.cdwater.ticketbox.cinema.domain.model.Cinema;
import com.cdwater.ticketbox.cinema.domain.repository.CinemaRepository;
import com.cdwater.ticketbox.cinema.domain.repository.ScreeningRepository;
import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.common.result.ResultCode;
import com.cdwater.ticketbox.movie.application.query.MovieBrief;
import com.cdwater.ticketbox.movie.application.query.MovieQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CinemaQueryService {

    private final CinemaRepository cinemaRepository;
    private final ScreeningRepository screeningRepository;
    private final MovieQueryService movieQueryService;

    public PageResult<CinemaListItemVO> list(CinemaListQuery query) {
        PageResult<Cinema> page = cinemaRepository.page(query.getName(), query.getPage(), query.getSize());
        List<CinemaListItemVO> records = page.getRecords().stream()
                .map(c -> new CinemaListItemVO(c.getId(), c.getName(), c.getAddress(), c.getPhone()))
                .toList();
        return PageResult.of(page.getTotal(), records, page.getPage(), page.getSize());
    }

    public CinemaDetailVO detail(Long cinemaId) {
        Cinema cinema = cinemaRepository.findById(cinemaId)
                .filter(c -> c.getStatus() != null && c.getStatus() == 1)
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "影院不存在或已停用"));

        List<Long> movieIds = screeningRepository.findFutureMovieIds(cinemaId);
        Map<Long, MovieBrief> movies = movieQueryService.findByIds(movieIds);
        List<CinemaMovieVO> movieVOs = movies.values().stream()
                .filter(m -> m.status() != null && m.status() == 1)
                .map(m -> new CinemaMovieVO(m.id(), m.title(), m.poster(), m.releaseDate(), m.duration()))
                .sorted(Comparator.comparing(CinemaMovieVO::getReleaseDate).reversed())
                .toList();

        CinemaDetailVO vo = new CinemaDetailVO();
        vo.setId(cinema.getId());
        vo.setName(cinema.getName());
        vo.setAddress(cinema.getAddress());
        vo.setPhone(cinema.getPhone());
        vo.setMovies(movieVOs);
        return vo;
    }
}
