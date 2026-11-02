package com.cdwater.ticketbox.show.adapter.out.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("t_show_order_seat")
public class ShowOrderSeatPO {
    private Long orderNo;
    private Long seatId;
    private String zone;
    private Integer seatNo;
    private BigDecimal price;
}
