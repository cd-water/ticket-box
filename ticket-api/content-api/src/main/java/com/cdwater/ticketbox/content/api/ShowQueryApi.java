package com.cdwater.ticketbox.content.api;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.content.api.dto.ShowDetailDTO;
import com.cdwater.ticketbox.content.api.dto.ShowListItemDTO;
import com.cdwater.ticketbox.content.api.dto.ShowListQueryDTO;

public interface ShowQueryApi {

    PageResult<ShowListItemDTO> list(ShowListQueryDTO query) throws BizException;

    ShowDetailDTO getDetail(long showId) throws BizException;
}
