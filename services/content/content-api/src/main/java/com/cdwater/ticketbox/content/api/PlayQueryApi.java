package com.cdwater.ticketbox.content.api;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.content.api.dto.PlayDetailDTO;
import com.cdwater.ticketbox.content.api.dto.PlayListItemDTO;
import com.cdwater.ticketbox.content.api.dto.PlayListQueryDTO;

public interface PlayQueryApi {

    PageResult<PlayListItemDTO> list(PlayListQueryDTO query) throws BizException;

    PlayDetailDTO detail(long playId) throws BizException;
}
