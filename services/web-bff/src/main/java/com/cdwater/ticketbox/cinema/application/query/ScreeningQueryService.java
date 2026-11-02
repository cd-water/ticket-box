package com.cdwater.ticketbox.cinema.application.query;

import com.cdwater.ticketbox.cinema.application.query.converter.ScreeningViewConverter;
import com.cdwater.ticketbox.cinema.application.query.view.ScreeningView;
import com.cdwater.ticketbox.cinema.application.query.view.SeatMapView;
import com.cdwater.ticketbox.cinema.application.query.view.SeatView;
import com.cdwater.ticketbox.cinema.application.query.ScreeningBookingInfo;
import com.cdwater.ticketbox.cinema.domain.model.Cinema;
import com.cdwater.ticketbox.cinema.domain.model.Hall;
import com.cdwater.ticketbox.cinema.domain.model.Screening;
import com.cdwater.ticketbox.cinema.domain.model.ScreeningDetails;
import com.cdwater.ticketbox.cinema.domain.repository.CinemaRepository;
import com.cdwater.ticketbox.cinema.domain.repository.HallRepository;
import com.cdwater.ticketbox.cinema.domain.repository.ScreeningRepository;
import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.ResultCode;
import com.cdwater.ticketbox.cinema.application.port.MovieInfo;
import com.cdwater.ticketbox.cinema.application.port.MovieInfoPort;
import com.cdwater.ticketbox.inventory.api.MovieSeatInventoryApi;
import com.cdwater.ticketbox.inventory.api.dto.MovieSeatDTO;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ScreeningQueryService {

    private final ScreeningRepository screeningRepository;
    private final CinemaRepository cinemaRepository;
    private final HallRepository hallRepository;
    private final MovieInfoPort movieInfoPort;
    private final ScreeningViewConverter screeningViewConverter;

    @DubboReference(check = false, timeout = 3000)
    private MovieSeatInventoryApi movieSeatInventoryApi;

    public List<ScreeningView> list(Long cinemaId, Long movieId) {
        List<ScreeningDetails> rows = screeningRepository.findFutureWithHall(cinemaId, movieId);
        List<Long> ids = rows.stream().map(ScreeningDetails::getId).toList();
        Map<Long, Long> counts = movieSeatInventoryApi.countAvailable(ids);
        return rows.stream()
                .map(r -> screeningViewConverter.toView(r, counts.getOrDefault(r.getId(), 0L)))
                .toList();
    }

    public SeatMapView seatMap(Long screeningId) {
        Screening screening = screeningRepository.findById(screeningId)
                .filter(Screening::isOnSale)
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "排场不存在或已取消"));
        Cinema cinema = cinemaRepository.findById(screening.getCinemaId())
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "影院不存在"));
        Hall hall = hallRepository.findById(screening.getHallId())
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "影厅不存在"));
        MovieInfo movie = movieInfoPort.findById(screening.getMovieId())
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "电影不存在"));

        List<MovieSeatDTO> snapshots = movieSeatInventoryApi.seatMap(screeningId);
        List<SeatView> seats = snapshots.stream()
                .map(screeningViewConverter::toView)
                .toList();

        SeatMapView view = screeningViewConverter.toView(screening, cinema, hall, movie);
        view.setRowCount(snapshots.stream().mapToInt(MovieSeatDTO::getRowNum).max().orElse(0));
        view.setColCount(snapshots.stream().mapToInt(MovieSeatDTO::getColNum).max().orElse(0));
        view.setSeats(seats);
        return view;
    }

    public ScreeningBookingInfo getBookingInfo(Long screeningId) {
        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "排场不存在"));
        Cinema cinema = cinemaRepository.findById(screening.getCinemaId())
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "影院不存在"));
        Hall hall = hallRepository.findById(screening.getHallId())
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "影厅不存在"));
        return new ScreeningBookingInfo(screening.getId(), screening.getCinemaId(), cinema.getName(),
                screening.getHallId(), hall.getName(), screening.getMovieId(),
                screening.getStartTime(), screening.getEndTime(), screening.getPrice(), screening.getStatus());
    }
}
