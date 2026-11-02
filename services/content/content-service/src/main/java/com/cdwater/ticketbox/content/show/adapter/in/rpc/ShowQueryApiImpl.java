package com.cdwater.ticketbox.content.show.adapter.in.rpc;

import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.content.api.ShowQueryApi;
import com.cdwater.ticketbox.content.api.dto.ShowDetailDTO;
import com.cdwater.ticketbox.content.api.dto.ShowListItemDTO;
import com.cdwater.ticketbox.content.api.dto.ShowListQueryDTO;
import com.cdwater.ticketbox.content.show.application.query.ShowQueryService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
@RequiredArgsConstructor
public class ShowQueryApiImpl implements ShowQueryApi {

    private final ShowQueryService showQueryService;

    @Override
    public PageResult<ShowListItemDTO> list(ShowListQueryDTO query) {
        return showQueryService.list(query);
    }

    @Override
    public ShowDetailDTO detail(long showId) {
        return showQueryService.detail(showId);
    }
}
