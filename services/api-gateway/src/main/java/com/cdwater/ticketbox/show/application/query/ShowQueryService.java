package com.cdwater.ticketbox.show.application.query;

import com.cdwater.ticketbox.shared.error.BizException;
import com.cdwater.ticketbox.shared.error.ResultCode;
import com.cdwater.ticketbox.shared.query.PageResult;
import com.cdwater.ticketbox.show.application.query.view.ShowDetailView;
import com.cdwater.ticketbox.show.application.query.view.ShowListItemView;
import com.cdwater.ticketbox.show.application.query.view.TicketTypeView;
import com.cdwater.ticketbox.show.domain.model.Show;
import com.cdwater.ticketbox.show.application.query.converter.ShowViewConverter;
import com.cdwater.ticketbox.show.domain.repository.ShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ShowQueryService {

    private final ShowRepository showRepository;
    private final ShowViewConverter showViewConverter;

    public PageResult<ShowListItemView> list(ShowListQuery query) {
        PageResult<Show> page = showRepository.page(query.getKeyword(), query.getPage(), query.getSize());
        Map<Long, BigDecimal> prices = showRepository.findMinPrices(
                page.getRecords().stream().map(Show::getId).toList());
        List<ShowListItemView> records = page.getRecords().stream().map(show -> {
            ShowListItemView view = showViewConverter.toListItemView(show);
            view.setMinPrice(prices.get(show.getId()));
            return view;
        }).toList();
        return PageResult.of(page.getTotal(), records, page.getPage(), page.getSize());
    }

    public ShowDetailView detail(Long id) {
        Show show = showRepository.findById(id)
                .filter(s -> s.isUpcoming(LocalDateTime.now()))
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "演出不存在或已开演"));
        Map<String, Long> availability = showRepository.countAvailableSeatsByZone(id);

        ShowDetailView view = showViewConverter.toDetailView(show);
        view.setTicketTypes(showRepository.findTicketTypes(id).stream().map(type -> {
            TicketTypeView typeView = showViewConverter.toTicketTypeView(type);
            typeView.setAvailableSeats(availability.getOrDefault(type.getZone(), 0L));
            return typeView;
        }).toList());
        return view;
    }
}
