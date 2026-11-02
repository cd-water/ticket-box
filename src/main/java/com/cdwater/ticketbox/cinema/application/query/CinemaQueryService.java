package com.cdwater.ticketbox.cinema.application.query;

import com.cdwater.ticketbox.cinema.application.port.MovieInfo;
import com.cdwater.ticketbox.cinema.application.port.MovieInfoPort;
import com.cdwater.ticketbox.cinema.application.query.converter.CinemaViewConverter;
import com.cdwater.ticketbox.cinema.application.query.view.CinemaDetailView;
import com.cdwater.ticketbox.cinema.application.query.view.CinemaListItemView;
import com.cdwater.ticketbox.cinema.application.query.view.CinemaMovieView;
import com.cdwater.ticketbox.cinema.domain.model.Cinema;
import com.cdwater.ticketbox.cinema.domain.repository.CinemaRepository;
import com.cdwater.ticketbox.cinema.domain.repository.ScreeningRepository;
import com.cdwater.ticketbox.shared.error.BizException;
import com.cdwater.ticketbox.shared.query.PageResult;
import com.cdwater.ticketbox.shared.error.ResultCode;
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

    public PageResult<CinemaListItemView> list(CinemaListQuery query) {
        PageResult<Cinema> page = cinemaRepository.page(
                query.getName(), query.getMovieId(), query.getPage(), query.getSize());
        List<CinemaListItemView> records = page.getRecords().stream()
                .map(cinemaViewMapper::toView)
                .toList();
        return PageResult.of(page.getTotal(), records, page.getPage(), page.getSize());
    }

    public CinemaDetailView detail(Long cinemaId) {
        Cinema cinema = cinemaRepository.findById(cinemaId)
                .filter(Cinema::isOperating)
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "影院不存在或已停用"));

        List<Long> movieIds = screeningRepository.findFutureMovieIds(cinemaId);
        Map<Long, MovieInfo> movies = movieInfoPort.findByIds(movieIds);
        List<CinemaMovieView> movieViews = movies.values().stream()
                .filter(m -> m.getStatus() != null && m.getStatus() == 1)
                .map(cinemaViewMapper::toView)
                .sorted(Comparator.comparing(CinemaMovieView::getReleaseDate).reversed())
                .toList();

        CinemaDetailView view = cinemaViewMapper.toDetailView(cinema);
        view.setMovies(movieViews);
        return view;
    }
}
