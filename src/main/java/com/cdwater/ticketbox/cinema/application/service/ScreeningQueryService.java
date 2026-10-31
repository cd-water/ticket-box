package com.cdwater.ticketbox.cinema.application.service;

import com.cdwater.ticketbox.cinema.api.vo.ScreeningVO;
import com.cdwater.ticketbox.cinema.api.vo.SeatMapVO;
import com.cdwater.ticketbox.cinema.api.vo.SeatVO;
import com.cdwater.ticketbox.cinema.application.dto.ScreeningBookingInfo;
import com.cdwater.ticketbox.cinema.domain.model.Cinema;
import com.cdwater.ticketbox.cinema.domain.model.Hall;
import com.cdwater.ticketbox.cinema.domain.model.Screening;
import com.cdwater.ticketbox.cinema.domain.model.SeatSnapshot;
import com.cdwater.ticketbox.cinema.domain.repository.CinemaRepository;
import com.cdwater.ticketbox.cinema.domain.repository.HallRepository;
import com.cdwater.ticketbox.cinema.domain.repository.ScreeningRepository;
import com.cdwater.ticketbox.cinema.domain.repository.ScreeningSeatRepository;
import com.cdwater.ticketbox.cinema.infrastructure.persistence.projection.ScreeningRow;
import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.ResultCode;
import com.cdwater.ticketbox.movie.application.query.MovieBrief;
import com.cdwater.ticketbox.movie.application.query.MovieQueryService;
import lombok.RequiredArgsConstructor;
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
    private final ScreeningSeatRepository screeningSeatRepository;
    private final SeatReservationService seatReservationService;
    private final MovieQueryService movieQueryService;

    public List<ScreeningVO> list(Long cinemaId, Long movieId) {
        List<ScreeningRow> rows = screeningRepository.findFutureWithHall(cinemaId, movieId);
        List<Long> ids = rows.stream().map(ScreeningRow::getId).toList();
        Map<Long, Long> counts = screeningRepository.countAvailableSeats(ids);
        return rows.stream()
                .map(r -> new ScreeningVO(r.getId(), r.getHallName(), r.getStartTime(),
                        r.getEndTime(), r.getPrice(), counts.getOrDefault(r.getId(), 0L)))
                .toList();
    }

    public SeatMapVO seatMap(Long screeningId) {
        seatReservationService.releaseExpiredByScreening(screeningId, LocalDateTime.now());
        Screening screening = screeningRepository.findById(screeningId)
                .filter(s -> s.getStatus() != null && s.getStatus() == 1)
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "排场不存在或已取消"));
        Cinema cinema = cinemaRepository.findById(screening.getCinemaId())
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "影院不存在"));
        Hall hall = hallRepository.findById(screening.getHallId())
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "影厅不存在"));
        MovieBrief movie = movieQueryService.findById(screening.getMovieId())
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "电影不存在"));

        List<SeatSnapshot> snapshots = screeningSeatRepository.findSeatMap(screeningId);
        List<SeatVO> seats = snapshots.stream()
                .map(s -> new SeatVO(s.getSeatId(), s.getRowNum(), s.getColNum(), s.getStatus()))
                .toList();

        SeatMapVO vo = new SeatMapVO();
        vo.setScreeningId(screeningId);
        vo.setCinemaName(cinema.getName());
        vo.setMovieTitle(movie.title());
        vo.setHallName(hall.getName());
        vo.setStartTime(screening.getStartTime());
        vo.setEndTime(screening.getEndTime());
        vo.setPrice(screening.getPrice());
        vo.setRowCount(snapshots.stream().mapToInt(SeatSnapshot::getRowNum).max().orElse(0));
        vo.setColCount(snapshots.stream().mapToInt(SeatSnapshot::getColNum).max().orElse(0));
        vo.setSeats(seats);
        return vo;
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
