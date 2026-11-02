package com.cdwater.ticketbox.content.adapter.in.rpc;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.ResultCode;
import com.cdwater.ticketbox.content.api.ContentCatalogApi;
import com.cdwater.ticketbox.content.api.dto.MovieBriefDTO;
import com.cdwater.ticketbox.content.api.dto.PlayItemDTO;
import com.cdwater.ticketbox.content.api.dto.ScreeningBookingDTO;
import com.cdwater.ticketbox.content.api.dto.ShowBookingDTO;
import com.cdwater.ticketbox.content.cinema.application.query.ScreeningQueryService;
import com.cdwater.ticketbox.content.movie.application.query.MovieBriefQueryService;
import com.cdwater.ticketbox.content.play.application.query.PlayQueryService;
import com.cdwater.ticketbox.content.show.application.query.ShowQueryService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
@RequiredArgsConstructor
public class ContentCatalogApiImpl implements ContentCatalogApi {

    private final ScreeningQueryService screeningQueryService;
    private final MovieBriefQueryService movieBriefQueryService;
    private final ShowQueryService showQueryService;
    private final PlayQueryService playQueryService;

    @Override
    public ScreeningBookingDTO getScreeningBooking(long screeningId) {
        return screeningQueryService.getBookingInfo(screeningId);
    }

    @Override
    public MovieBriefDTO getMovie(long movieId) {
        return movieBriefQueryService.findById(movieId)
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "电影不存在或已下架"));
    }

    @Override
    public ShowBookingDTO getShowBooking(long showId, long ticketTypeId) {
        return showQueryService.getBooking(showId, ticketTypeId);
    }

    @Override
    public PlayItemDTO getPlayItem(long itemId) {
        return playQueryService.getBooking(itemId);
    }
}
