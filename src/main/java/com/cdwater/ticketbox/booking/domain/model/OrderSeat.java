package com.cdwater.ticketbox.booking.domain.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_order_seat")
public class OrderSeat {

    private Long orderId;
    private Long seatId;
    private Integer rowNum;
    private Integer colNum;
    private BigDecimal price;
    private LocalDateTime createTime;
}
