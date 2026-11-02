package com.cdwater.ticketbox.play.application.query;

import com.cdwater.ticketbox.play.application.query.view.PlayDetailView;
import com.cdwater.ticketbox.play.application.query.view.PlayListItemView;
import com.cdwater.ticketbox.play.domain.model.PlayItem;
import com.cdwater.ticketbox.play.application.query.converter.PlayViewConverter;
import com.cdwater.ticketbox.play.domain.repository.PlayRepository;
import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.ResultCode;
import com.cdwater.ticketbox.common.result.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayQueryService {

    private final PlayRepository playRepository;
    private final PlayViewConverter playViewConverter;

    public PageResult<PlayListItemView> list(PlayListQuery query) {
        PageResult<PlayItem> page = playRepository.page(
                query.getKeyword(), query.getPage(), query.getSize());
        List<PlayListItemView> records = page.getRecords().stream()
                .map(playViewConverter::toListItemView)
                .toList();
        return PageResult.of(page.getTotal(), records, page.getPage(), page.getSize());
    }

    public PlayDetailView detail(Long id) {
        PlayItem item = playRepository.findById(id)
                .filter(PlayItem::isOnSale)
                .orElseThrow(() -> new BizException(ResultCode.NOT_FOUND, "玩乐项目不存在或已下架"));
        PlayDetailView view = playViewConverter.toDetailView(item);
        view.setDescription(item.getDescription());
        return view;
    }

}
