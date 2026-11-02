package com.cdwater.ticketbox.order.domain.model;

import com.cdwater.ticketbox.common.ticket.ProductType;
import com.cdwater.ticketbox.content.api.dto.BookingSnapshotDTO;
import com.cdwater.ticketbox.inventory.api.dto.InventoryUnitDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class BookingOrder {

    /** 支付窗口：电影 15 分钟 / 演出 5 分钟 / 玩乐 30 分钟。 */
    public static final Duration MOVIE_TTL = Duration.ofMinutes(15);
    public static final Duration SHOW_TTL = Duration.ofMinutes(5);
    public static final Duration PLAY_TTL = Duration.ofMinutes(30);

    private Long orderNo;
    private Long userId;
    private ProductType productType;
    private Integer status;
    private BigDecimal totalAmount;
    private LocalDateTime expireTime;
    private LocalDateTime payTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private BookingLine line;
    private List<BookingSeat> seats = new ArrayList<>();

    public static Duration ttlOf(ProductType productType) {
        return switch (productType) {
            case MOVIE -> MOVIE_TTL;
            case SHOW -> SHOW_TTL;
            case PLAY -> PLAY_TTL;
        };
    }

    /** 创建订单：商品信息在创建时定格，订单不依赖目录的实时状态。 */
    public static BookingOrder place(Long orderNo, Long userId, BookingSnapshotDTO snapshot,
                                     int quantity, LocalDateTime now) {
        BookingOrder order = new BookingOrder();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setProductType(snapshot.getProductType());
        order.setStatus(BookingStatus.PENDING.getCode());
        order.setTotalAmount(snapshot.getUnitPrice().multiply(BigDecimal.valueOf(quantity)));
        order.setExpireTime(now.plus(ttlOf(snapshot.getProductType())));
        order.setLine(lineOf(orderNo, snapshot, quantity));
        return order;
    }

    private static BookingLine lineOf(Long orderNo, BookingSnapshotDTO snapshot, int quantity) {
        BookingLine line = new BookingLine();
        line.setOrderNo(orderNo);
        line.setProductId(snapshot.getProductId());
        line.setTicketTypeId(snapshot.getTicketTypeId());
        line.setTitle(snapshot.getTitle());
        line.setVenueName(snapshot.getVenueName());
        line.setVenueAddress(snapshot.getVenueAddress());
        line.setHallName(snapshot.getHallName());
        line.setTicketTypeName(snapshot.getTicketTypeName());
        line.setZone(snapshot.getZone());
        line.setStartTime(snapshot.getStartTime());
        line.setEndTime(snapshot.getEndTime());
        line.setUnitPrice(snapshot.getUnitPrice());
        line.setQuantity(quantity);
        return line;
    }

    /** 由库存预留返回的座位单元构建订单座位；数字库存没有单元。 */
    public void attachSeats(List<InventoryUnitDTO> units) {
        this.seats = units.stream().map(unit -> {
            BookingSeat seat = new BookingSeat();
            seat.setOrderNo(orderNo);
            seat.setSeatId(unit.getSeatId());
            seat.setZone(unit.getZone());
            seat.setSeatNo(unit.getSeatNo());
            seat.setRowNum(unit.getRowNum());
            seat.setColNum(unit.getColNum());
            seat.setPrice(line.getUnitPrice());
            return seat;
        }).toList();
    }

    public boolean isPending() {
        return Integer.valueOf(BookingStatus.PENDING.getCode()).equals(status);
    }

    public boolean isPaid() {
        return Integer.valueOf(BookingStatus.PAID.getCode()).equals(status);
    }

    public boolean isExpired(LocalDateTime now) {
        return isPending() && !expireTime.isAfter(now);
    }
}
