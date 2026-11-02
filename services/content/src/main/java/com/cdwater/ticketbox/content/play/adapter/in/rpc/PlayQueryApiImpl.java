package com.cdwater.ticketbox.content.play.adapter.in.rpc;

import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.content.api.PlayQueryApi;
import com.cdwater.ticketbox.content.api.dto.PlayDetailDTO;
import com.cdwater.ticketbox.content.api.dto.PlayListItemDTO;
import com.cdwater.ticketbox.content.api.dto.PlayListQueryDTO;
import com.cdwater.ticketbox.content.play.application.query.PlayQueryService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
@RequiredArgsConstructor
public class PlayQueryApiImpl implements PlayQueryApi {

    private final PlayQueryService playQueryService;

    @Override
    public PageResult<PlayListItemDTO> list(PlayListQueryDTO query) {
        return playQueryService.list(query);
    }

    @Override
    public PlayDetailDTO getDetail(long playId) {
        return playQueryService.getDetail(playId);
    }
}
