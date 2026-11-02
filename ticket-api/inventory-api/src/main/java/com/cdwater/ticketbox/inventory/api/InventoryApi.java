package com.cdwater.ticketbox.inventory.api;

import com.cdwater.ticketbox.common.exception.BizException;
import com.cdwater.ticketbox.inventory.api.dto.HoldCommandDTO;
import com.cdwater.ticketbox.inventory.api.dto.HoldDTO;
import com.cdwater.ticketbox.inventory.api.dto.MovieSeatDTO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 统一库存边界：读可用量 + 预留（hold）/ 确认（confirm）/ 释放（release）。
 * 三种商品在服务内部由不同策略实现，对外只有一个契约。
 */
public interface InventoryApi {

    List<MovieSeatDTO> getSeatMap(long screeningId);

    Map<Long, Long> countAvailableSeats(Collection<Long> screeningIds);

    Map<String, Long> countAvailableZones(long showId);

    Map<Long, Integer> getPlayStocks(Collection<Long> itemIds);

    /** 幂等占位：同一 orderNo 重复调用返回同一预留。 */
    HoldDTO hold(HoldCommandDTO command) throws BizException;

    /** 幂等确认：预留转为已售/已扣减。 */
    void confirm(long orderNo) throws BizException;

    /** 幂等释放：未售释放、已售回滚、数字库存恢复。 */
    void release(long orderNo) throws BizException;
}
