package com.cdwater.ticketbox.home.adapter.out.client;

import com.cdwater.ticketbox.home.adapter.out.client.converter.PlayHomeConverter;
import com.cdwater.ticketbox.home.application.port.HomePlayPort;
import com.cdwater.ticketbox.home.application.query.view.HomePlayItem;
import com.cdwater.ticketbox.play.application.query.PlayListQuery;
import com.cdwater.ticketbox.play.application.query.PlayQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PlayHomeAdapter implements HomePlayPort {

    private final PlayQueryService playQueryService;
    private final PlayHomeConverter playHomeConverter;

    @Override
    public List<HomePlayItem> findOnSale(int limit) {
        PlayListQuery query = new PlayListQuery();
        query.setKeyword("");
        query.setPage(1);
        query.setSize(limit);
        return playQueryService.list(query).getRecords().stream().map(play -> {
            HomePlayItem item = new HomePlayItem();
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
