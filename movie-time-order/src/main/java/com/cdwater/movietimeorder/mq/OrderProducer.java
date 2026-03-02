package com.cdwater.movietimeorder.mq;

import com.cdwater.movietimecommon.constants.RabbitMqConstant;
import com.cdwater.movietimeorder.mq.message.TtlMsg;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderProducer {

    @Resource
    private RabbitTemplate rabbitTemplate;

    public void releaseSeatAndCancelOrder(Long orderNo) {
        TtlMsg message = new TtlMsg(orderNo);
        rabbitTemplate.convertAndSend(
                RabbitMqConstant.BUSINESS_EXCHANGE,
                RabbitMqConstant.ORDER_TTL_ROUTING_KEY,
                message
        );
    }
}
