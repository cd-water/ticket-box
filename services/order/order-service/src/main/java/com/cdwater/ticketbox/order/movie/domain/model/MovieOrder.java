package com.cdwater.ticketbox.order.movie.domain.model;

import com.cdwater.ticketbox.inventory.api.dto.MovieSeatSnapshotDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class MovieOrder {

    /** 支付窗口：15 分钟 */
    public static final Duration PAYMENT_TTL = Duration.ofMinutes(15);

    private Long orderNo;
    private Long userId;
    private Long screeningId;
    private String movieTitle;
    private String cinemaName;
    private String hallName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal totalAmount;
    /** 0-待支付 1-已支付 2-已取消（含超时） */
    private Integer status;
    private LocalDateTime expireTime;
    private LocalDateTime payTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public boolean isPending() {
        return Integer.valueOf(MovieOrderStatus.PENDING.getCode()).equals(status);
    }

    public boolean isExpired(LocalDateTime now) {
        return isPending() && !expireTime.isAfter(now);
    }

    public boolean canPay(LocalDateTime now) {
        return isPending() && !isExpired(now);
    }

    /** 创建电影订单：15 分钟支付窗口，总额 = 票价 × 座位数。 */
    public static MovieOrder place(Long orderNo, Long userId, Long screeningId,
                                   String movieTitle, String cinemaName, String hallName,
                                   LocalDateTime startTime, LocalDateTime endTime,
                                   BigDecimal unitPrice, int seatCount, LocalDateTime now) {
        MovieOrder order = new MovieOrder();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setScreeningId(screeningId);
        order.setMovieTitle(movieTitle);
        order.setCinemaName(cinemaName);
        order.setHallName(hallName);
        order.setStartTime(startTime);
        order.setEndTime(endTime);
        order.setTotalAmount(unitPrice.multiply(BigDecimal.valueOf(seatCount)));
        order.setStatus(MovieOrderStatus.PENDING.getCode());
        order.setExpireTime(now.plus(PAYMENT_TTL));
        return order;
    }

    /** 由锁定的座位快照构建订单座位。 */
    public static List<MovieOrderSeat> seatsOf(Long orderNo, List<MovieSeatSnapshotDTO> snapshots,
                                               BigDecimal unitPrice) {
        return snapshots.stream().map(snapshot -> {
            MovieOrderSeat seat = new MovieOrderSeat();
            seat.setOrderNo(orderNo);
            seat.setSeatId(snapshot.getId());
            seat.setRowNum(snapshot.getRowNum());
            seat.setColNum(snapshot.getColNum());
            seat.setPrice(unitPrice);
            return seat;
        }).toList();
    }
}
