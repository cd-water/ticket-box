package com.cdwater.ticketbox.play.application.query;

import com.cdwater.ticketbox.play.application.query.view.PlayDetailView;
import com.cdwater.ticketbox.play.application.query.view.PlayListItemView;
import com.cdwater.ticketbox.play.domain.model.PlayItem;
import com.cdwater.ticketbox.play.application.query.converter.PlayViewConverter;
import com.cdwater.ticketbox.play.domain.repository.PlayRepository;
import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.ResultCode;
import com.cdwater.ticketbox.common.result.PageResult;
import com.cdwater.ticketbox.inventory.api.PlayStockApi;
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
    private PlayStockApi playStockApi;

    public PageResult<PlayListItemView> list(PlayListQuery query) {
        PageResult<PlayItem> page = playRepository.page(
                query.getKeyword(), query.getPage(), query.getSize());
        List<PlayListItemView> records = page.getRecords().stream()
                .map(playViewConverter::toListItemView)
                .toList();
        Map<Long, Integer> stocks = playStockApi.getStocks(
                records.stream().map(PlayListItemView::getId).toList());
        records.forEach(v -> v.setStock(stocks.getOrDefault(v.getId(), 0)));
        return PageResult.of(page.getTotal(), records, page.getPage(), page.getSize());
    }

    public PlayDetailView detail(Long id) {
        PlayItem item = playRepository.findById(id)
                .filter(PlayItem::isOnSale)
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "玩乐项目不存在或已下架"));
        PlayDetailView view = playViewConverter.toDetailView(item);
        view.setDescription(item.getDescription());
        view.setStock(playStockApi.getStocks(List.of(item.getId()))
                .getOrDefault(item.getId(), 0));
        return view;
    }

}
