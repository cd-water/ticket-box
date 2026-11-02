package com.cdwater.ticketbox.play.domain.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PlayOrder {

    private Long orderNo;
    private Long userId;
    private Long itemId;
    private String itemTitle;
    private String itemAddress;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal totalAmount;
    private Integer status;
    private LocalDateTime expireTime;
    private LocalDateTime payTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;


    /** 支付窗口：30 分钟 */
    public static final java.time.Duration PAYMENT_TTL = java.time.Duration.ofMinutes(30);

    public boolean isPending() {
        return Integer.valueOf(PlayOrderStatus.PENDING.getCode()).equals(status);
    }

    public boolean isExpired(java.time.LocalDateTime now) {
        return isPending() && !expireTime.isAfter(now);
    }

    public boolean canPay(java.time.LocalDateTime now) {
        return isPending() && !isExpired(now);
    }

    /** 创建玩乐订单：下单即扣库存，30 分钟支付窗口。 */
    public static PlayOrder place(Long orderNo, Long userId, PlayItem item,
                                  int quantity, LocalDateTime now) {
        PlayOrder order = new PlayOrder();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setItemId(item.getId());
        order.setItemTitle(item.getTitle());
        order.setItemAddress(item.getAddress());
        order.setUnitPrice(item.getPrice());
        order.setQuantity(quantity);
        order.setTotalAmount(item.getPrice().multiply(BigDecimal.valueOf(quantity)));
        order.setStatus(PlayOrderStatus.PENDING.getCode());
        order.setExpireTime(now.plus(PAYMENT_TTL));
        return order;
    }
}
