package com.cdwater.ticketbox.home.adapter.out.client;

import com.cdwater.ticketbox.home.adapter.out.client.converter.ShowHomeConverter;
import com.cdwater.ticketbox.home.application.port.HomeShowPort;
import com.cdwater.ticketbox.home.application.query.view.HomeShowItem;
import com.cdwater.ticketbox.show.application.query.ShowListQuery;
import com.cdwater.ticketbox.show.application.query.ShowQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ShowHomeAdapter implements HomeShowPort {

    private final ShowQueryService showQueryService;
    private final ShowHomeConverter showHomeConverter;

    @Override
    public List<HomeShowItem> findUpcoming(int limit) {
        ShowListQuery query = new ShowListQuery();
        query.setKeyword("");
        query.setPage(1);
        query.setSize(limit);
        return showQueryService.list(query).getRecords().stream().map(show -> {
            HomeShowItem item = showHomeConverter.toItem(show);
            item.setMinPrice(show.getMinPrice());
            return item;
        }).toList();
    }
}
