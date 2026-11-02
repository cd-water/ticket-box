package com.cdwater.ticketbox.order.adapter.out.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_booking_line")
public class BookingLinePO {

    @TableId(value = "order_no", type = IdType.INPUT)
    private Long orderNo;
    private Long productId;
    private Long ticketTypeId;
    private String title;
    private String venueName;
    private String venueAddress;
    private String hallName;
    private String ticketTypeName;
    private String zone;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal unitPrice;
    private Integer quantity;
}
