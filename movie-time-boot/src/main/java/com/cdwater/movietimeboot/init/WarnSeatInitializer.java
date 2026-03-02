package com.cdwater.movietimeboot.init;

import com.cdwater.movietimecinema.mapper.ScreeningMapper;
import com.cdwater.movietimecinema.model.entity.Screening;
import com.cdwater.movietimecommon.constants.RedisConstant;
import com.cdwater.movietimeorder.mapper.OrderMapper;
import com.cdwater.movietimeorder.model.entity.Order;
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
public class WarnSeatInitializer implements ApplicationRunner {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("缓存预热占用座位...");
        //获取最近24小时的非取消订单
        List<Order> list = orderMapper.selectRecentlyOneDay();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        //分批预热
        int total = list.size();
        int batchSize = 1000;//每批1000条
        for (int i = 0; i < total; i += batchSize) {
            int end = Math.min(i + batchSize, total);
            List<Order> batch = list.subList(i, end);
            //redis管道预热
            redisTemplate.executePipelined(new SessionCallback<>() {
                @Override
                public Object execute(RedisOperations operations) throws DataAccessException {
                    for (Order order : batch) {
                        //用ticketCount暂存colCount，偷懒
                        Integer colCount = order.getTicketCount();
                        //解析座位信息
                        String[] seats = order.getSeatInfo().split(",");
                        for (String seat : seats) {
                            String[] rowAndCol = seat.split("_");
                            int row = Integer.parseInt(rowAndCol[0]);
                            int col = Integer.parseInt(rowAndCol[1]);
                            long offset = (long) colCount * (row - 1) + (col - 1);
                            //还原锁定座位
                            operations.opsForValue().setBit(
                                    RedisConstant.SEAT_SCREENING + ":" + order.getScreeningId(),
                                    offset, true);
                        }
                    }
                    return null;
                }
            });
        }
    }
}
