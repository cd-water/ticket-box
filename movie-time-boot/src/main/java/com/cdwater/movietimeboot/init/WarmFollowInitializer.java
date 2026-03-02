package com.cdwater.movietimeboot.init;

import com.cdwater.movietimecommon.constants.RedisConstant;
import com.cdwater.movietimeuser.mapper.UserMapper;
import com.cdwater.movietimeuser.model.entity.UserFollow;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class WarmFollowInitializer implements ApplicationRunner {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("缓存预热用户关注关系...");
        List<UserFollow> list = userMapper.selectFollow();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        //分批预热
        int total = list.size();
        int batchSize = 1000;//每批1000条
        for (int i = 0; i < total; i += batchSize) {
            int end = Math.min(i + batchSize, total);
            List<UserFollow> batch = list.subList(i, end);
            //redis管道预热
            redisTemplate.executePipelined(new SessionCallback<>() {
                @Override
                public Object execute(RedisOperations operations) throws DataAccessException {
                    batch.forEach(userFollow -> {
                        Long followerId = userFollow.getFollowerId();
                        Long followeeId = userFollow.getFolloweeId();
                        //关注列表
                        operations.opsForSet().add(
                                RedisConstant.USER_FOLLOWER + ":" + followerId,
                                followeeId.toString());
                        //粉丝列表
                        operations.opsForSet().add(
                                RedisConstant.USER_FAN + ":" + followeeId,
                                followerId.toString());
                    });
                    return null;
                }
            });
        }
    }
}
