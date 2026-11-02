package com.cdwater.ticketbox.inventory.adapter.out.persistence;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.ResultCode;
import com.cdwater.ticketbox.inventory.adapter.out.persistence.converter.ShowSeatConverter;
import com.cdwater.ticketbox.inventory.adapter.out.persistence.mapper.ShowSeatMapper;
import com.cdwater.ticketbox.inventory.adapter.out.persistence.po.ShowSeatPO;
import com.cdwater.ticketbox.inventory.adapter.out.persistence.projection.ZoneAvailabilityRow;
import com.cdwater.ticketbox.inventory.application.command.ShowSeatPicker;
import com.cdwater.ticketbox.inventory.domain.model.ShowSeat;
import com.cdwater.ticketbox.inventory.domain.repository.ShowSeatInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ShowSeatInventoryRepositoryImpl implements ShowSeatInventoryRepository {

    private final ShowSeatMapper showSeatMapper;
    private final ShowSeatConverter showSeatConverter;

    @Override
    public Map<String, Long> countAvailableByZone(Long showId) {
        return showSeatMapper.countAvailableByZone(showId).stream()
                .collect(Collectors.toMap(ZoneAvailabilityRow::getZone,
                        ZoneAvailabilityRow::getAvailable));
    }

    @Override
    public List<ShowSeat> lockSeats(Long showId, String zone, int quantity,
                                    Long orderNo, LocalDateTime expireTime) {
        List<ShowSeat> available = showSeatConverter.toDomain(
                showSeatMapper.lockAvailableSeats(showId, zone, LocalDateTime.now()));
        if (available.size() < quantity) {
            throw new BizException(ResultCode.CONFLICT, "该区域余票不足");
        }
        List<ShowSeat> picked = ShowSeatPicker.pick(available, quantity);
        showSeatMapper.lockSeats(picked.stream().map(ShowSeat::getId).toList(),
                orderNo, expireTime);
        return picked;
    }

    @Override
    public List<ShowSeat> findLockedByOrder(Long orderNo) {
        return showSeatConverter.toDomain(showSeatMapper.selectLockedByOrder(orderNo));
    }

    @Override
    public int confirmByOrder(Long orderNo) {
        return showSeatMapper.confirmByOrder(orderNo);
    }

    @Override
    public int releaseByOrder(Long orderNo) {
        return showSeatMapper.releaseByOrder(orderNo);
    }
}
