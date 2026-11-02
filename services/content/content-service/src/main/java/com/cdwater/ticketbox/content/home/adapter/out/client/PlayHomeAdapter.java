package com.cdwater.ticketbox.content.home.adapter.out.client;
import com.cdwater.ticketbox.content.api.dto.PlayListQueryDTO;

import com.cdwater.ticketbox.content.home.adapter.out.client.converter.PlayHomeConverter;
import com.cdwater.ticketbox.content.home.application.port.HomePlayPort;
import com.cdwater.ticketbox.content.api.dto.HomePlayDTO;
import com.cdwater.ticketbox.content.play.application.query.PlayQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PlayHomeAdapter implements HomePlayPort {

    private final PlayQueryService playQueryService;
    private final PlayHomeConverter playHomeConverter;

    @Override
    public List<HomePlayDTO> findOnSale(int limit) {
        PlayListQueryDTO query = new PlayListQueryDTO();
        query.setKeyword("");
        query.setPage(1);
        query.setSize(limit);
        return playQueryService.list(query).getRecords().stream().map(play -> {
            HomePlayDTO item = new HomePlayDTO();
            item.setId(play.getId());
            item.setTitle(play.getTitle());
            item.setCover(play.getCover());
            item.setAddress(play.getAddress());
            item.setPrice(play.getPrice());
            item.setStock(play.getStock());
            return item;
        }).toList();
    }
}