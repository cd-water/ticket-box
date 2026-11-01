package com.cdwater.ticketbox.booking.domain.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("t_movie_order_seat")
public class OrderSeat {

    private Long orderNo;
    private Long seatId;
    private Integer rowNum;
    private Integer colNum;
    private BigDecimal price;
}
