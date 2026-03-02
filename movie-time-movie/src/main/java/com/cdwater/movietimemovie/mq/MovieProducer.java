package com.cdwater.movietimemovie.mq;

import com.cdwater.movietimecommon.constants.RabbitMqConstant;
import com.cdwater.movietimemovie.mapper.ReviewMapper;
import com.cdwater.movietimemovie.mq.message.LikeMsg;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * 电影模块生产者
 */
@Component
public class MovieProducer {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private ReviewMapper reviewMapper;

    public void persistLike(Long userId, Long reviewId, Long type) {
        LikeMsg message = new LikeMsg(userId, reviewId, type);
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMqConstant.BUSINESS_EXCHANGE,
                    RabbitMqConstant.MOVIE_REVIEWLIKE_ROUTING_KEY,
                    message
            );
        } catch (Exception e) {
            //降级处理，同步持久化mysql
            if (type == 0L) {
                //已点赞，取消点赞
                reviewMapper.deleteLike(userId, reviewId);
            } else if (type == 1L) {
                //未点赞，点赞
                reviewMapper.insertLike(userId, reviewId);
            }
        }
    }
}
