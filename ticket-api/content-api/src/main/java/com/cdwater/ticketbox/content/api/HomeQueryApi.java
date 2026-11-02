package com.cdwater.ticketbox.content.api;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.content.api.dto.HomeDTO;

public interface HomeQueryApi {

    HomeDTO getHome() throws BizException;
}
