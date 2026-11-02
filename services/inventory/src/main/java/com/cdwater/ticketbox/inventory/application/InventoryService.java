package com.cdwater.ticketbox.inventory.application;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.common.result.ResultCode;
import com.cdwater.ticketbox.common.ticket.ProductType;
import com.cdwater.ticketbox.inventory.api.dto.HoldCommandDTO;
import com.cdwater.ticketbox.inventory.api.dto.HoldDTO;
import com.cdwater.ticketbox.inventory.api.dto.InventoryUnitDTO;
import com.cdwater.ticketbox.inventory.domain.model.Hold;
import com.cdwater.ticketbox.inventory.domain.model.HoldStatus;
import com.cdwater.ticketbox.inventory.domain.model.MovieSeatSnapshot;
import com.cdwater.ticketbox.inventory.domain.model.ShowSeat;
import com.cdwater.ticketbox.inventory.domain.repository.HoldRepository;
import com.cdwater.ticketbox.inventory.domain.repository.PlayStockRepository;
import com.cdwater.ticketbox.inventory.domain.repository.MovieSeatInventoryRepository;
import com.cdwater.ticketbox.inventory.domain.repository.ShowSeatInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final MovieSeatInventoryRepository movieSeats;
    private final ShowSeatInventoryRepository showSeats;
    private final PlayStockRepository playStock;
    private final HoldRepository holdRepository;

    public List<MovieSeatSnapshot> getSeatMap(Long screeningId) {
        return movieSeats.findSeatMap(screeningId);
    }

    public Map<Long, Long> countAvailableMovieSeats(Collection<Long> screeningIds) {
        return movieSeats.countAvailableSeats(screeningIds);
    }

    public Map<String, Long> countAvailableShowSeats(Long showId) {
        return showSeats.countAvailableByZone(showId);
    }

    public Map<Long, Integer> getPlayStocks(Collection<Long> itemIds) {
        return playStock.getStocks(itemIds);
    }

    /**
     * 幂等占位：先写 t_hold 抢占订单号，再锁座位/扣数字库存。
     * 同一 orderNo 的并发请求由主键串行化，只有第一个请求真正操作库存。
     */
    @Transactional
    public HoldDTO hold(HoldCommandDTO command) {
        validate(command);

        Optional<Hold> existing = holdRepository.findByOrderNo(command.getOrderNo());
        if (existing.isPresent()) {
            return existingHold(existing.get());
        }

        Hold hold = new Hold();
        hold.setOrderNo(command.getOrderNo());
        hold.setProductType(command.getProductType());
        hold.setProductId(command.getProductId());
        hold.setZone(command.getZone());
        hold.setQuantity(quantityOf(command));
        hold.setStatus(HoldStatus.HELD.getCode());
        hold.setExpireTime(command.getExpireTime());

        try {
            holdRepository.insert(hold);
        } catch (DuplicateKeyException e) {
            Hold winner = holdRepository.findByOrderNo(command.getOrderNo())
                    .orElseThrow(() -> new BizException(ResultCode.CONFLICT, "库存预留冲突"));
            return existingHold(winner);
        }

        List<InventoryUnitDTO> units = switch (command.getProductType()) {
            case MOVIE -> holdMovieSeats(command);
            case SHOW -> holdShowSeats(command);
            case PLAY -> holdPlayStock(command);
        };

        HoldDTO result = new HoldDTO();
        result.setOrderNo(command.getOrderNo());
        result.setProductType(command.getProductType());
        result.setUnits(units);
        return result;
    }

    @Transactional
    public void confirm(Long orderNo) {
        Hold hold = holdRepository.findByOrderNo(orderNo).orElse(null);
        if (hold == null || hold.getStatus() == HoldStatus.CONFIRMED.getCode()) {
            return;
        }
        if (hold.getStatus() == HoldStatus.RELEASED.getCode()) {
            throw new BizException(ResultCode.CONFLICT, "库存预留已释放");
        }
        switch (hold.getProductType()) {
            case MOVIE -> movieSeats.markSoldByOrder(orderNo);
            case SHOW -> showSeats.confirmByOrder(orderNo);
            case PLAY -> {
                // 数字库存在占位时已扣减
            }
        }
        holdRepository.markConfirmed(orderNo);
    }

    @Transactional
    public void release(Long orderNo) {
        Hold hold = holdRepository.findByOrderNo(orderNo).orElse(null);
        if (hold == null) {
            movieSeats.releaseByOrder(orderNo);
            showSeats.releaseByOrder(orderNo);
            return;
        }
        if (hold.getStatus() == HoldStatus.RELEASED.getCode()) {
            return;
        }
        switch (hold.getProductType()) {
            case MOVIE -> movieSeats.releaseByOrder(orderNo);
            case SHOW -> showSeats.releaseByOrder(orderNo);
            case PLAY -> playStock.restore(hold.getProductId(), hold.getQuantity());
        }
        holdRepository.markReleased(orderNo);
    }

    private HoldDTO existingHold(Hold hold) {
        if (hold.getStatus() == HoldStatus.RELEASED.getCode()) {
            throw new BizException(ResultCode.CONFLICT, "库存预留已释放");
        }
        HoldDTO result = new HoldDTO();
        result.setOrderNo(hold.getOrderNo());
        result.setProductType(hold.getProductType());
        result.setUnits(unitsOf(hold));
        return result;
    }

    private List<InventoryUnitDTO> unitsOf(Hold hold) {
        return switch (hold.getProductType()) {
            case MOVIE -> movieSeats.findLockedByOrder(hold.getOrderNo()).stream()
                    .map(this::toUnit).toList();
            case SHOW -> showSeats.findLockedByOrder(hold.getOrderNo()).stream()
                    .map(this::toUnit).toList();
            case PLAY -> List.of();
        };
    }

    private List<InventoryUnitDTO> holdMovieSeats(HoldCommandDTO command) {
        List<Long> seatIds = command.getSeatIds().stream().distinct().toList();
        List<MovieSeatSnapshot> rows = movieSeats.lockSeats(command.getProductId(), seatIds);
        if (rows.size() != seatIds.size()) {
            throw new BizException(ResultCode.NOT_FOUND, "座位不存在或不属于该排场");
        }
        LocalDateTime now = LocalDateTime.now();
        for (MovieSeatSnapshot row : rows) {
            boolean available = row.getStatus() == 0
                    || (row.getStatus() == 1
                        && row.getLockExpireTime() != null
                        && row.getLockExpireTime().isBefore(now));
            if (!available) {
                throw new BizException(ResultCode.CONFLICT, "座位已被占用");
            }
        }
        movieSeats.updateLocks(command.getProductId(), seatIds,
                command.getOrderNo(), command.getExpireTime());
        return rows.stream().map(this::toUnit).toList();
    }

    private List<InventoryUnitDTO> holdShowSeats(HoldCommandDTO command) {
        return showSeats.lockSeats(command.getProductId(), command.getZone(),
                        command.getQuantity(), command.getOrderNo(), command.getExpireTime())
                .stream().map(this::toUnit).toList();
    }

    private List<InventoryUnitDTO> holdPlayStock(HoldCommandDTO command) {
        if (!playStock.deduct(command.getProductId(), command.getQuantity())) {
            throw new BizException(ResultCode.CONFLICT, "余票不足");
        }
        return List.of();
    }

    private Integer quantityOf(HoldCommandDTO command) {
        return command.getProductType() == ProductType.MOVIE
                ? command.getSeatIds().size()
                : command.getQuantity();
    }

    private void validate(HoldCommandDTO command) {
        if (command.getProductType() == null || command.getProductId() == null
                || command.getOrderNo() == null || command.getExpireTime() == null) {
            throw new BizException(ResultCode.BAD_REQUEST, "库存预留参数不完整");
        }
        switch (command.getProductType()) {
            case MOVIE -> {
                List<Long> seatIds = command.getSeatIds();
                if (seatIds == null || seatIds.isEmpty() || seatIds.size() > 6
                        || seatIds.stream().anyMatch(id -> id == null || id <= 0)) {
                    throw new BizException(ResultCode.BAD_REQUEST, "请选择 1-6 个有效座位");
                }
                if (seatIds.stream().distinct().count() != seatIds.size()) {
                    throw new BizException(ResultCode.BAD_REQUEST, "座位不能重复");
                }
            }
            case SHOW -> {
                if (command.getZone() == null || command.getZone().isBlank()) {
                    throw new BizException(ResultCode.BAD_REQUEST, "演出区域不能为空");
                }
                if (command.getQuantity() == null
                        || command.getQuantity() < 1 || command.getQuantity() > 6) {
                    throw new BizException(ResultCode.BAD_REQUEST, "请选择 1-6 张票");
                }
            }
            case PLAY -> {
                if (command.getQuantity() == null
                        || command.getQuantity() < 1 || command.getQuantity() > 10) {
                    throw new BizException(ResultCode.BAD_REQUEST, "请选择 1-10 份");
                }
            }
        }
    }

    private InventoryUnitDTO toUnit(MovieSeatSnapshot seat) {
        return new InventoryUnitDTO(seat.getSeatId(), null, null,
                seat.getRowNum(), seat.getColNum());
    }

    private InventoryUnitDTO toUnit(ShowSeat seat) {
        return new InventoryUnitDTO(seat.getId(), seat.getZone(), seat.getSeatNo(),
                null, null);
    }
}
