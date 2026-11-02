package com.cdwater.ticketbox.content.show.application.query;
import com.cdwater.ticketbox.content.api.dto.ShowListQueryDTO;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.ResultCode;
import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.content.api.dto.ShowBookingDTO;
import com.cdwater.ticketbox.content.api.dto.ShowDetailDTO;
import com.cdwater.ticketbox.content.api.dto.ShowListItemDTO;
import com.cdwater.ticketbox.content.api.dto.TicketTypeDTO;
import com.cdwater.ticketbox.content.show.domain.model.Show;
import com.cdwater.ticketbox.content.show.domain.model.ShowTicketType;
import com.cdwater.ticketbox.content.show.application.query.converter.ShowViewConverter;
import com.cdwater.ticketbox.content.show.domain.repository.ShowRepository;
import com.cdwater.ticketbox.inventory.api.ShowSeatInventoryApi;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
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

    @DubboReference(check = false, timeout = 3000)
    private ShowSeatInventoryApi showSeatInventoryApi;

    public PageResult<ShowListItemDTO> list(ShowListQueryDTO query) {
        PageResult<Show> page = showRepository.page(query.getKeyword(), query.getPage(), query.getSize());
        Map<Long, BigDecimal> prices = showRepository.findMinPrices(
                page.getRecords().stream().map(Show::getId).toList());
        List<ShowListItemDTO> records = page.getRecords().stream().map(show -> {
            ShowListItemDTO view = showViewConverter.toListItemView(show);
            view.setMinPrice(prices.get(show.getId()));
            return view;
        }).toList();
        return PageResult.of(page.getTotal(), records, page.getPage(), page.getSize());
    }

    public ShowDetailDTO detail(Long id) {
        Show show = showRepository.findById(id)
                .filter(s -> s.isUpcoming(LocalDateTime.now()))
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "演出不存在或已开演"));
        Map<String, Long> availability = showSeatInventoryApi.countAvailable(id);

        ShowDetailDTO view = showViewConverter.toDetailView(show);
        view.setTicketTypes(showRepository.findTicketTypes(id).stream().map(type -> {
            TicketTypeDTO typeView = showViewConverter.toTicketTypeView(type);
            typeView.setAvailableSeats(availability.getOrDefault(type.getZone(), 0L));
            return typeView;
        }).toList());
        return view;
    }

    public ShowBookingDTO getBooking(Long showId, Long ticketTypeId) {
        LocalDateTime now = LocalDateTime.now();
        Show show = showRepository.findById(showId)
                .filter(s -> s.isUpcoming(now))
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "演出不存在或已开演"));
        ShowTicketType type = showRepository.findTicketType(showId, ticketTypeId)
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "票档不存在或已停售"));
        return new ShowBookingDTO(show.getId(), show.getTitle(), show.getVenueName(),
                show.getVenueAddress(), show.getStartTime(), type.getId(), type.getName(),
                type.getZone(), type.getPrice(), true);
    }
}