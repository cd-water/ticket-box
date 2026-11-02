package com.cdwater.ticketbox.content.cinema.application.query;

import com.cdwater.ticketbox.content.cinema.application.query.converter.ScreeningViewConverter;
import com.cdwater.ticketbox.content.api.dto.ScreeningDTO;
import com.cdwater.ticketbox.content.api.dto.SeatMapDTO;
import com.cdwater.ticketbox.content.api.dto.SeatDTO;
import com.cdwater.ticketbox.content.api.dto.ScreeningBookingDTO;
import com.cdwater.ticketbox.content.cinema.domain.model.Cinema;
import com.cdwater.ticketbox.content.cinema.domain.model.Hall;
import com.cdwater.ticketbox.content.cinema.domain.model.Screening;
import com.cdwater.ticketbox.content.cinema.domain.model.ScreeningDetails;
import com.cdwater.ticketbox.content.cinema.domain.repository.CinemaRepository;
import com.cdwater.ticketbox.content.cinema.domain.repository.HallRepository;
import com.cdwater.ticketbox.content.cinema.domain.repository.ScreeningRepository;
import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.ResultCode;
import com.cdwater.ticketbox.content.cinema.application.port.MovieInfo;
import com.cdwater.ticketbox.content.cinema.application.port.MovieInfoPort;
import com.cdwater.ticketbox.inventory.api.InventoryApi;
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
    private InventoryApi inventoryApi;

    public List<ScreeningDTO> list(Long cinemaId, Long movieId) {
        List<ScreeningDetails> rows = screeningRepository.findFutureWithHall(cinemaId, movieId);
        List<Long> ids = rows.stream().map(ScreeningDetails::getId).toList();
        Map<Long, Long> counts = inventoryApi.countAvailableSeats(ids);
        return rows.stream()
                .map(row -> screeningViewConverter.toView(row, counts.getOrDefault(row.getId(), 0L)))
                .toList();
    }

    public SeatMapDTO getSeatMap(Long screeningId) {
        Screening screening = screeningRepository.findById(screeningId)
                .filter(Screening::isOnSale)
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "排场不存在或已取消"));
        Cinema cinema = cinemaRepository.findById(screening.getCinemaId())
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "影院不存在"));
        Hall hall = hallRepository.findById(screening.getHallId())
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "影厅不存在"));
        MovieInfo movie = movieInfoPort.findById(screening.getMovieId())
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "电影不存在"));

        List<MovieSeatDTO> snapshots = inventoryApi.getSeatMap(screeningId);
        List<SeatDTO> seats = snapshots.stream()
                .map(screeningViewConverter::toView)
                .toList();

        SeatMapDTO view = screeningViewConverter.toView(screening, cinema, hall, movie);
        view.setRowCount(snapshots.stream().mapToInt(MovieSeatDTO::getRowNum).max().orElse(0));
        view.setColCount(snapshots.stream().mapToInt(MovieSeatDTO::getColNum).max().orElse(0));
        view.setSeats(seats);
        return view;
    }

    public ScreeningBookingDTO getBooking(Long screeningId) {
        Screening screening = screeningRepository.findById(screeningId)
                .filter(candidate -> candidate.isBookable(LocalDateTime.now()))
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "排场不存在或已不可售"));
        Cinema cinema = cinemaRepository.findById(screening.getCinemaId())
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "影院不存在"));
        Hall hall = hallRepository.findById(screening.getHallId())
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "影厅不存在"));
        return new ScreeningBookingDTO(screening.getId(), screening.getCinemaId(), cinema.getName(),
                screening.getHallId(), hall.getName(), screening.getMovieId(),
                screening.getStartTime(), screening.getEndTime(), screening.getPrice(), screening.getStatus());
    }
}