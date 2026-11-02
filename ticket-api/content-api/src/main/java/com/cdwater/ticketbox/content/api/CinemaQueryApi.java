package com.cdwater.ticketbox.content.api;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.content.api.dto.CinemaDetailDTO;
import com.cdwater.ticketbox.content.api.dto.CinemaListItemDTO;
import com.cdwater.ticketbox.content.api.dto.CinemaListQueryDTO;

public interface CinemaQueryApi {

    PageResult<CinemaListItemDTO> list(CinemaListQueryDTO query) throws BizException;

    CinemaDetailDTO getDetail(long cinemaId) throws BizException;
}
