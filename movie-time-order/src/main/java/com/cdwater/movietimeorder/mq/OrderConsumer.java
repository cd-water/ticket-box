package com.cdwater.movietimeorder.mq;

import com.cdwater.movietimecommon.constants.RabbitMqConstant;
import com.cdwater.movietimeorder.mq.message.TtlMsg;
import com.cdwater.movietimeorder.service.OrderService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderConsumer {

    @Resource
    private OrderService orderService;

    @RabbitListener(queues = RabbitMqConstant.ORDER_RELEASE_DLQ)
    public void consumeTtlMsg(TtlMsg message) {
        Long orderNo = message.getOrderNo();
        boolean isSuccess = orderService.cancelMQ(orderNo);
        if (isSuccess) {
            log.info("取消订单成功，订单号：{}", orderNo);
        } else {
            log.warn("取消订单失败，订单号：{}", orderNo);
        }
    }
}
