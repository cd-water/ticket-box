package com.cdwater.ticketbox.order.adapter.out.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("t_booking_seat")
public class BookingSeatPO {

    private Long orderNo;
    private Long seatId;
    private String zone;
    private Integer seatNo;
    private Integer rowNum;
    private Integer colNum;
    private BigDecimal price;
}
