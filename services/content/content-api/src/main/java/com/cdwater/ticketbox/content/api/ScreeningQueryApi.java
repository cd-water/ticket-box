package com.cdwater.ticketbox.content.api;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.content.api.dto.ScreeningDTO;
import com.cdwater.ticketbox.content.api.dto.SeatMapDTO;

import java.util.List;

public interface ScreeningQueryApi {

    List<ScreeningDTO> list(Long cinemaId, Long movieId) throws BizException;

    SeatMapDTO seatMap(long screeningId) throws BizException;
}
