package com.cdwater.ticketbox.show.domain.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ShowOrder {

    private Long orderNo;
    private Long userId;
    private Long showId;
    private String showTitle;
    private String venueName;
    private String venueAddress;
    private LocalDateTime startTime;
    private Long ticketTypeId;
    private String ticketTypeName;
    private String zone;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal totalAmount;
    private Integer status;
    private LocalDateTime expireTime;
    private LocalDateTime payTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;


    /** 支付窗口：5 分钟 */
    public static final java.time.Duration PAYMENT_TTL = java.time.Duration.ofMinutes(5);

    public boolean isPending() {
        return Integer.valueOf(ShowOrderStatus.PENDING.getCode()).equals(status);
    }

    public boolean isExpired(java.time.LocalDateTime now) {
        return isPending() && !expireTime.isAfter(now);
    }

    public boolean canPay(java.time.LocalDateTime now) {
        return isPending() && !isExpired(now);
    }

    /** 创建演出订单：单票档 + 数量，5 分钟支付窗口。 */
    public static ShowOrder place(Long orderNo, Long userId, Long showId,
                                  String showTitle, String venueName, String venueAddress,
                                  LocalDateTime startTime, ShowTicketType ticketType,
                                  int quantity, LocalDateTime now) {
        ShowOrder order = new ShowOrder();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setShowId(showId);
        order.setShowTitle(showTitle);
        order.setVenueName(venueName);
        order.setVenueAddress(venueAddress);
        order.setStartTime(startTime);
        order.setTicketTypeId(ticketType.getId());
        order.setTicketTypeName(ticketType.getName());
        order.setZone(ticketType.getZone());
        order.setUnitPrice(ticketType.getPrice());
        order.setQuantity(quantity);
        order.setTotalAmount(ticketType.getPrice().multiply(BigDecimal.valueOf(quantity)));
        order.setStatus(ShowOrderStatus.PENDING.getCode());
        order.setExpireTime(now.plus(PAYMENT_TTL));
        return order;
    }
}
