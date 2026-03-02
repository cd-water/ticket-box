package com.cdwater.movietimemovie.mq;

import com.cdwater.movietimecommon.constants.RabbitMqConstant;
import com.cdwater.movietimemovie.mapper.ReviewMapper;
import com.cdwater.movietimemovie.mq.message.LikeMsg;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 电影模块消费者
 */
@Component
public class MovieConsumer {

    @Resource
    private ReviewMapper reviewMapper;

    @RabbitListener(queues = RabbitMqConstant.MOVIE_REVIEWLIKE_QUEUE)
    public void consumeLikeMsg(LikeMsg message) {
        Long userId = message.getUserId();
        Long reviewId = message.getReviewId();
        Long type = message.getType();
        if (type == 0L) {
            //已点赞，取消点赞
            reviewMapper.deleteLike(userId, reviewId);
        } else if (type == 1L) {
            //未点赞，点赞
            reviewMapper.insertLike(userId, reviewId);
        }
    }
}
