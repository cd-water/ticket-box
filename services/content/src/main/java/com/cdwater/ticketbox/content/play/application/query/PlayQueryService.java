package com.cdwater.ticketbox.content.play.application.query;
import com.cdwater.ticketbox.content.api.dto.PlayListQueryDTO;

import com.cdwater.ticketbox.content.api.dto.PlayDetailDTO;
import com.cdwater.ticketbox.content.api.dto.PlayItemDTO;
import com.cdwater.ticketbox.content.api.dto.PlayListItemDTO;
import com.cdwater.ticketbox.content.play.domain.model.PlayItem;
import com.cdwater.ticketbox.content.play.application.query.converter.PlayViewConverter;
import com.cdwater.ticketbox.content.play.domain.repository.PlayRepository;
import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.ResultCode;
import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.inventory.api.InventoryApi;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PlayQueryService {

    private final PlayRepository playRepository;
    private final PlayViewConverter playViewConverter;

    @DubboReference(check = false, timeout = 3000)
    private InventoryApi inventoryApi;

    public PageResult<PlayListItemDTO> list(PlayListQueryDTO query) {
        PageResult<PlayItem> page = playRepository.page(
                query.getKeyword(), query.getPage(), query.getSize());
        List<PlayListItemDTO> records = page.getRecords().stream()
                .map(playViewConverter::toListItemView)
                .toList();
        Map<Long, Integer> stocks = inventoryApi.getPlayStocks(
                records.stream().map(PlayListItemDTO::getId).toList());
        records.forEach(view -> view.setStock(stocks.getOrDefault(view.getId(), 0)));
        return PageResult.of(page.getTotal(), records, page.getPage(), page.getSize());
    }

    public PlayDetailDTO getDetail(Long id) {
        PlayItem item = playRepository.findById(id)
                .filter(PlayItem::isOnSale)
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "玩乐项目不存在或已下架"));
        PlayDetailDTO view = playViewConverter.toDetailView(item);
        view.setDescription(item.getDescription());
        view.setStock(inventoryApi.getPlayStocks(List.of(item.getId()))
                .getOrDefault(item.getId(), 0));
        return view;
    }

    public PlayItemDTO getBooking(Long itemId) {
        PlayItem item = playRepository.findById(itemId)
                .filter(PlayItem::isOnSale)
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "玩乐项目不存在或已下架"));
        return new PlayItemDTO(item.getId(), item.getTitle(), item.getCover(),
                item.getAddress(), item.getDescription(), item.getPrice(), true);
    }
}