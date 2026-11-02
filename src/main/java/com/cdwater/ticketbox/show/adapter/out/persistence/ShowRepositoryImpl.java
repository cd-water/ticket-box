package com.cdwater.ticketbox.show.adapter.out.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cdwater.ticketbox.shared.persistence.LikeUtils;
import com.cdwater.ticketbox.shared.query.PageResult;
import com.cdwater.ticketbox.show.adapter.out.persistence.mapper.ShowMapper;
import com.cdwater.ticketbox.show.adapter.out.persistence.mapper.ShowSeatMapper;
import com.cdwater.ticketbox.show.adapter.out.persistence.mapper.ShowTicketTypeMapper;
import com.cdwater.ticketbox.show.domain.model.Show;
import com.cdwater.ticketbox.show.domain.model.ShowTicketType;
import com.cdwater.ticketbox.show.domain.repository.ShowRepository;
import com.cdwater.ticketbox.show.adapter.out.persistence.converter.ShowConverter;
import com.cdwater.ticketbox.show.adapter.out.persistence.converter.ShowTicketTypeConverter;
import com.cdwater.ticketbox.show.adapter.out.persistence.po.ShowPO;
import com.cdwater.ticketbox.show.adapter.out.persistence.po.ShowTicketTypePO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ShowRepositoryImpl implements ShowRepository {

    private final ShowMapper showMapper;
    private final ShowConverter showConverter;
    private final ShowTicketTypeConverter ticketTypeConverter;
    private final ShowTicketTypeMapper ticketTypeMapper;
    private final ShowSeatMapper showSeatMapper;

    @Override
    public PageResult<Show> page(String keyword, long page, long size) {
        LambdaQueryWrapper<ShowPO> wrapper = new LambdaQueryWrapper<ShowPO>()
                .eq(ShowPO::getStatus, Show.STATUS_ON_SALE)
                .gt(ShowPO::getStartTime, LocalDateTime.now())
                .orderByAsc(ShowPO::getStartTime)
                .orderByAsc(ShowPO::getId);
        String trimmed = keyword == null ? "" : keyword.trim();
        if (!trimmed.isEmpty()) {
            wrapper.like(ShowPO::getTitle, LikeUtils.escape(trimmed));
        }
        Page<ShowPO> result = showMapper.selectPage(new Page<>(page, size), wrapper);
        return PageResult.of(result.getTotal(), showConverter.toDomain(result.getRecords()),
                result.getCurrent(), result.getSize());
    }

    @Override
    public Optional<Show> findById(Long id) {
        return Optional.ofNullable(showMapper.selectById(id)).map(showConverter::toDomain);
    }

    @Override
    public List<ShowTicketType> findTicketTypes(Long showId) {
        return ticketTypeConverter.toDomain(ticketTypeMapper.selectList(new LambdaQueryWrapper<ShowTicketTypePO>()
                .eq(ShowTicketTypePO::getShowId, showId)
                .eq(ShowTicketTypePO::getStatus, ShowTicketType.STATUS_ON_SALE)
                .orderByAsc(ShowTicketTypePO::getSortOrder)
                .orderByAsc(ShowTicketTypePO::getId)));
    }

    @Override
    public Optional<ShowTicketType> findTicketType(Long showId, Long ticketTypeId) {
        return Optional.ofNullable(ticketTypeConverter.toDomain(ticketTypeMapper.selectOne(new LambdaQueryWrapper<ShowTicketTypePO>()
                .eq(ShowTicketTypePO::getId, ticketTypeId)
                .eq(ShowTicketTypePO::getShowId, showId)
                .eq(ShowTicketTypePO::getStatus, ShowTicketType.STATUS_ON_SALE))));
    }

    @Override
    public Map<Long, BigDecimal> findMinPrices(Collection<Long> showIds) {
        if (showIds == null || showIds.isEmpty()) {
            return Map.of();
        }
        List<ShowTicketType> types = ticketTypeConverter.toDomain(ticketTypeMapper.selectList(new LambdaQueryWrapper<ShowTicketTypePO>()
                .in(ShowTicketTypePO::getShowId, showIds)
                .eq(ShowTicketTypePO::getStatus, ShowTicketType.STATUS_ON_SALE)));
        Map<Long, BigDecimal> prices = new HashMap<>();
        for (ShowTicketType type : types) {
            prices.merge(type.getShowId(), type.getPrice(), BigDecimal::min);
        }
        return prices;
    }

    @Override
    public Map<String, Long> countAvailableSeatsByZone(Long showId) {
        Map<String, Long> result = new HashMap<>();
        showSeatMapper.countAvailableByZone(showId)
                .forEach(row -> result.put(row.getZone(), row.getAvailable()));
        return result;
    }
}
