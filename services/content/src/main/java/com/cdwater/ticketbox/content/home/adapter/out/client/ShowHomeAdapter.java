package com.cdwater.ticketbox.content.home.adapter.out.client;
import com.cdwater.ticketbox.content.api.dto.ShowListQueryDTO;

import com.cdwater.ticketbox.content.home.adapter.out.client.converter.ShowHomeConverter;
import com.cdwater.ticketbox.content.home.application.port.HomeShowPort;
import com.cdwater.ticketbox.content.api.dto.HomeShowDTO;
import com.cdwater.ticketbox.content.show.application.query.ShowQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ShowHomeAdapter implements HomeShowPort {

    private final ShowQueryService showQueryService;
    private final ShowHomeConverter showHomeConverter;

    @Override
    public List<HomeShowDTO> findUpcoming(int limit) {
        ShowListQueryDTO query = new ShowListQueryDTO();
        query.setKeyword("");
        query.setPage(1);
        query.setSize(limit);
        return showQueryService.list(query).getRecords().stream().map(show -> {
            HomeShowDTO item = showHomeConverter.toItem(show);
            item.setMinPrice(show.getMinPrice());
            return item;
        }).toList();
    }
}