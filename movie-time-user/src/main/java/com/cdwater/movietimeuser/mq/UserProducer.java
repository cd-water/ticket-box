package com.cdwater.movietimeuser.mq;

import com.cdwater.movietimecommon.constants.RabbitMqConstant;
import com.cdwater.movietimeuser.mapper.UserMapper;
import com.cdwater.movietimeuser.mq.message.CodeMsg;
import com.cdwater.movietimeuser.mq.message.FollowMsg;
import com.cdwater.movietimeuser.service.SmsService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * 用户模块生产者
 */
@Component
public class UserProducer {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private SmsService smsService;

    @Resource
    private UserMapper userMapper;

    public void sendCode(String phone, String code) {
        try {
            CodeMsg message = new CodeMsg(phone, code);
            rabbitTemplate.convertAndSend(
                    RabbitMqConstant.BUSINESS_EXCHANGE,
                    RabbitMqConstant.USER_CODE_ROUTING_KEY,
                    message
            );
        } catch (Exception e) {
            //降级处理，同步发送
            smsService.send(phone, code);
        }
    }

    public void persistFollow(Long followerId, Long followeeId, Long type) {
        FollowMsg message = new FollowMsg(followerId, followeeId, type);
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMqConstant.BUSINESS_EXCHANGE,
                    RabbitMqConstant.USER_FOLLOW_ROUTING_KEY,
                    message
            );
        } catch (Exception e) {
            //降级处理，同步持久化mysql
            if (type == 0L) {
                //已关注，进行取关
                userMapper.deleteFollow(followerId, followeeId);
            } else if (type == 1L) {
                //未关注，进行关注
                userMapper.insertFollow(followerId, followeeId);
            }
        }
    }
}
