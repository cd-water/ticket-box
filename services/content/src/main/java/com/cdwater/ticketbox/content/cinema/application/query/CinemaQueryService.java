package com.cdwater.ticketbox.content.cinema.application.query;
import com.cdwater.ticketbox.content.api.dto.CinemaListQueryDTO;

import com.cdwater.ticketbox.content.cinema.application.port.MovieInfo;
import com.cdwater.ticketbox.content.cinema.application.port.MovieInfoPort;
import com.cdwater.ticketbox.content.cinema.application.query.converter.CinemaViewConverter;
import com.cdwater.ticketbox.content.api.dto.CinemaDetailDTO;
import com.cdwater.ticketbox.content.api.dto.CinemaListItemDTO;
import com.cdwater.ticketbox.content.api.dto.CinemaMovieDTO;
import com.cdwater.ticketbox.content.cinema.domain.model.Cinema;
import com.cdwater.ticketbox.content.cinema.domain.repository.CinemaRepository;
import com.cdwater.ticketbox.content.cinema.domain.repository.ScreeningRepository;
import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.common.result.ResultCode;
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
    private final MovieInfoPort movieInfoPort;
    private final CinemaViewConverter cinemaViewMapper;

    public PageResult<CinemaListItemDTO> list(CinemaListQueryDTO query) {
        PageResult<Cinema> page = cinemaRepository.page(
                query.getName(), query.getMovieId(), query.getPage(), query.getSize());
        List<CinemaListItemDTO> records = page.getRecords().stream()
                .map(cinemaViewMapper::toView)
                .toList();
        return PageResult.of(page.getTotal(), records, page.getPage(), page.getSize());
    }

    public CinemaDetailDTO getDetail(Long cinemaId) {
        Cinema cinema = cinemaRepository.findById(cinemaId)
                .filter(Cinema::isOperating)
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "影院不存在或已停用"));

        List<Long> movieIds = screeningRepository.findFutureMovieIds(cinemaId);
        Map<Long, MovieInfo> movies = movieInfoPort.findByIds(movieIds);
        List<CinemaMovieDTO> movieViews = movies.values().stream()
                .filter(movie -> movie.getStatus() != null && movie.getStatus() == 1)
                .map(cinemaViewMapper::toView)
                .sorted(Comparator.comparing(CinemaMovieDTO::getReleaseDate).reversed())
                .toList();

        CinemaDetailDTO view = cinemaViewMapper.toDetailView(cinema);
        view.setMovies(movieViews);
        return view;
    }
}