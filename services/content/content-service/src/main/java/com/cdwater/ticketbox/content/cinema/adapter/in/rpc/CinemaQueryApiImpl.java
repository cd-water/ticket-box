package com.cdwater.ticketbox.content.cinema.adapter.in.rpc;

import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.content.api.CinemaQueryApi;
import com.cdwater.ticketbox.content.api.dto.CinemaDetailDTO;
import com.cdwater.ticketbox.content.api.dto.CinemaListItemDTO;
import com.cdwater.ticketbox.content.api.dto.CinemaListQueryDTO;
import com.cdwater.ticketbox.content.cinema.application.query.CinemaQueryService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
@RequiredArgsConstructor
public class CinemaQueryApiImpl implements CinemaQueryApi {

    private final CinemaQueryService cinemaQueryService;

    @Override
    public PageResult<CinemaListItemDTO> list(CinemaListQueryDTO query) {
        return cinemaQueryService.list(query);
    }

    @Override
    public CinemaDetailDTO detail(long cinemaId) {
        return cinemaQueryService.detail(cinemaId);
    }
}
