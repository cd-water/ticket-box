package com.cdwater.movietimeuser.mq;

import com.cdwater.movietimecommon.constants.RabbitMqConstant;
import com.cdwater.movietimeuser.mapper.UserMapper;
import com.cdwater.movietimeuser.mq.message.CodeMsg;
import com.cdwater.movietimeuser.mq.message.FollowMsg;
import com.cdwater.movietimeuser.service.SmsService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 用户模块消费者
 */
@Component
public class UserConsumer {

    @Resource
    private SmsService smsService;

    @Resource
    private UserMapper userMapper;

    @RabbitListener(queues = RabbitMqConstant.USER_CODE_QUEUE)
    public void consumeCodeMsg(CodeMsg message) {
        smsService.send(message.getPhone(), message.getCode());
    }

    @RabbitListener(queues = RabbitMqConstant.USER_FOLLOW_QUEUE)
    public void consumeFollowMsg(FollowMsg message) {
        Long followerId = message.getFollowerId();
        Long followeeId = message.getFolloweeId();
        Long type = message.getType();
        if (type == 0L) {
            //已关注，进行取关
            userMapper.deleteFollow(followerId, followeeId);
        } else if (type == 1L) {
            //未关注，进行关注
            userMapper.insertFollow(followerId, followeeId);
        }
    }
}
