package com.cdwater.movietimeboot.init;

import com.cdwater.movietimecommon.constants.RedisConstant;
import com.cdwater.movietimemovie.mapper.ReviewMapper;
import com.cdwater.movietimemovie.model.entity.ReviewLike;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class WarmReviewInitializer implements ApplicationRunner {

    @Resource
    private ReviewMapper reviewMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("缓存预热影评点赞...");
        List<ReviewLike> list = reviewMapper.selectLike();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        //分批预热
        int total = list.size();
        int batchSize = 1000;//每批1000条
        for (int i = 0; i < total; i += batchSize) {
            int end = Math.min(i + batchSize, total);
            List<ReviewLike> batch = list.subList(i, end);
            //redis管道预热
            redisTemplate.executePipelined(new SessionCallback<>() {
                @Override
                public Object execute(RedisOperations operations) throws DataAccessException {
                    batch.forEach(reviewLike -> {
                        //预热点赞
                        operations.opsForSet().add(
                                RedisConstant.REVIEW_LIKE + ":" + reviewLike.getReviewId(),
                                reviewLike.getUserId().toString());
                    });
                    return null;
                }
            });
        }
    }
}
