package com.cdwater.ticketbox.content.cinema.adapter.in.rpc;

import com.cdwater.ticketbox.content.api.ScreeningQueryApi;
import com.cdwater.ticketbox.content.api.dto.ScreeningDTO;
import com.cdwater.ticketbox.content.api.dto.SeatMapDTO;
import com.cdwater.ticketbox.content.cinema.application.query.ScreeningQueryService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;

@DubboService
@RequiredArgsConstructor
public class ScreeningQueryApiImpl implements ScreeningQueryApi {

    private final ScreeningQueryService screeningQueryService;

    @Override
    public List<ScreeningDTO> list(Long cinemaId, Long movieId) {
        return screeningQueryService.list(cinemaId, movieId);
    }

    @Override
    public SeatMapDTO getSeatMap(long screeningId) {
        return screeningQueryService.getSeatMap(screeningId);
    }
}
