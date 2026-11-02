package com.cdwater.ticketbox.content.api;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.content.api.dto.MovieBriefDTO;
import com.cdwater.ticketbox.content.api.dto.PlayItemDTO;
import com.cdwater.ticketbox.content.api.dto.ScreeningBookingDTO;
import com.cdwater.ticketbox.content.api.dto.ShowBookingDTO;

public interface ContentCatalogApi {

    ScreeningBookingDTO getScreeningBooking(long screeningId) throws BizException;

    MovieBriefDTO getMovie(long movieId) throws BizException;

    ShowBookingDTO getShowBooking(long showId, long ticketTypeId) throws BizException;

    PlayItemDTO getPlayItem(long itemId) throws BizException;
}
