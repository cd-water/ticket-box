package com.cdwater.movietimeboot.init;

import com.cdwater.movietimecinema.mapper.CinemaMapper;
import com.cdwater.movietimecinema.model.entity.Cinema;
import com.cdwater.movietimecommon.constants.RedisConstant;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class WarmGeoInitializer implements ApplicationRunner {

    @Resource
    private CinemaMapper cinemaMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("缓存预热影院地理...");
        List<Cinema> list = cinemaMapper.selectList();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        //批量构建数据
        Map<Object, Point> memberCoordinateMap = new HashMap<>();
        list.forEach(cinema -> {
            Point point = new Point(cinema.getLongitude().doubleValue(), cinema.getLatitude().doubleValue());
            memberCoordinateMap.put(cinema.getId(), point);
        });
        //Redis预热
        redisTemplate.opsForGeo().add(RedisConstant.CINEMA_GEO, memberCoordinateMap);
    }
}
