package com.cdwater.ticketbox.content.adapter.in.rpc;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.ResultCode;
import com.cdwater.ticketbox.common.ticket.ProductType;
import com.cdwater.ticketbox.content.api.CatalogApi;
import com.cdwater.ticketbox.content.api.dto.BookingSnapshotDTO;
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
public class CatalogApiImpl implements CatalogApi {

    private final ScreeningQueryService screeningQueryService;
    private final MovieBriefQueryService movieBriefQueryService;
    private final ShowQueryService showQueryService;
    private final PlayQueryService playQueryService;

    @Override
    public BookingSnapshotDTO getBookingSnapshot(ProductType productType, long productId, Long ticketTypeId) {
        return switch (productType) {
            case MOVIE -> movieSnapshot(productId);
            case SHOW -> showSnapshot(productId, ticketTypeId);
            case PLAY -> playSnapshot(productId);
        };
    }

    private BookingSnapshotDTO movieSnapshot(long screeningId) {
        ScreeningBookingDTO info = screeningQueryService.getBooking(screeningId);
        MovieBriefDTO movie = movieBriefQueryService.findById(info.getMovieId())
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "电影不存在或已下架"));
        if (movie.getStatus() == null || movie.getStatus() != 1) {
            throw new BizException(ResultCode.NOT_FOUND, "电影不存在或已下架");
        }
        BookingSnapshotDTO snapshot = new BookingSnapshotDTO();
        snapshot.setProductType(ProductType.MOVIE);
        snapshot.setProductId(info.getScreeningId());
        snapshot.setTitle(movie.getTitle());
        snapshot.setVenueName(info.getCinemaName());
        snapshot.setHallName(info.getHallName());
        snapshot.setStartTime(info.getStartTime());
        snapshot.setEndTime(info.getEndTime());
        snapshot.setUnitPrice(info.getPrice());
        return snapshot;
    }

    private BookingSnapshotDTO showSnapshot(long showId, Long ticketTypeId) {
        if (ticketTypeId == null) {
            throw new BizException(ResultCode.BAD_REQUEST, "请选择演出票档");
        }
        ShowBookingDTO booking = showQueryService.getBooking(showId, ticketTypeId);
        BookingSnapshotDTO snapshot = new BookingSnapshotDTO();
        snapshot.setProductType(ProductType.SHOW);
        snapshot.setProductId(booking.getShowId());
        snapshot.setTicketTypeId(booking.getTicketTypeId());
        snapshot.setTitle(booking.getTitle());
        snapshot.setVenueName(booking.getVenueName());
        snapshot.setVenueAddress(booking.getVenueAddress());
        snapshot.setTicketTypeName(booking.getTicketTypeName());
        snapshot.setZone(booking.getZone());
        snapshot.setStartTime(booking.getStartTime());
        snapshot.setUnitPrice(booking.getPrice());
        return snapshot;
    }

    private BookingSnapshotDTO playSnapshot(long itemId) {
        PlayItemDTO item = playQueryService.getBooking(itemId);
        BookingSnapshotDTO snapshot = new BookingSnapshotDTO();
        snapshot.setProductType(ProductType.PLAY);
        snapshot.setProductId(item.getId());
        snapshot.setTitle(item.getTitle());
        snapshot.setVenueAddress(item.getAddress());
        snapshot.setUnitPrice(item.getPrice());
        return snapshot;
    }
}
