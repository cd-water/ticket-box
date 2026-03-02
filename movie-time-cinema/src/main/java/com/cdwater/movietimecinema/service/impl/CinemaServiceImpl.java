package com.cdwater.movietimecinema.service.impl;

import com.cdwater.movietimecinema.mapper.CinemaMapper;
import com.cdwater.movietimecinema.model.dto.CinemaAddDTO;
import com.cdwater.movietimecinema.model.dto.CinemaPageDTO;
import com.cdwater.movietimecinema.model.dto.CinemaUpdateDTO;
import com.cdwater.movietimecinema.model.entity.Cinema;
import com.cdwater.movietimecinema.model.vo.CinemaDetailVO;
import com.cdwater.movietimecinema.model.vo.CinemaListVO;
import com.cdwater.movietimecinema.model.vo.CinemaPageVO;
import com.cdwater.movietimecinema.model.vo.CinemaQueryVO;
import com.cdwater.movietimecinema.service.CinemaService;
import com.cdwater.movietimecommon.constants.RedisConstant;
import com.cdwater.movietimecommon.enums.RetEnum;
import com.cdwater.movietimecommon.exceptions.BusinessException;
import com.cdwater.movietimecommon.utils.RedisUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class CinemaServiceImpl implements CinemaService {

    @Resource
    private CinemaMapper cinemaMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private RedissonClient redissonClient;

    @Override
    public void add(CinemaAddDTO cinemaAddDTO) {
        Cinema cinema = Cinema.builder()
                .name(cinemaAddDTO.getName())
                .address(cinemaAddDTO.getAddress())
                .longitude(cinemaAddDTO.getLongitude())
                .latitude(cinemaAddDTO.getLatitude())
                .phone(cinemaAddDTO.getPhone())
                .tags(cinemaAddDTO.getTags())
                .build();
        //mysql新增
        cinemaMapper.insert(cinema);
        //redis写入对应geo缓存
        Point point = new Point(cinema.getLongitude().doubleValue(), cinema.getLatitude().doubleValue());
        redisTemplate.opsForGeo().add(RedisConstant.CINEMA_GEO, point, cinema.getId());
        //清除缓存
        redisTemplate.delete(RedisConstant.CINEMA_LIST);
    }

    @Override
    public void delete(Long id) {
        //mysql删除
        cinemaMapper.deleteById(id);
        //redis清除对应geo缓存
        redisTemplate.opsForGeo().remove(RedisConstant.CINEMA_GEO, id);
        //清除缓存
        redisTemplate.delete(RedisConstant.CINEMA_LIST);
    }

    @Override
    public void update(CinemaUpdateDTO cinemaUpdateDTO) {
        Cinema cinema = Cinema.builder()
                .id(cinemaUpdateDTO.getId())
                .name(cinemaUpdateDTO.getName())
                .address(cinemaUpdateDTO.getAddress())
                .longitude(cinemaUpdateDTO.getLongitude())
                .latitude(cinemaUpdateDTO.getLatitude())
                .phone(cinemaUpdateDTO.getPhone())
                .tags(cinemaUpdateDTO.getTags())
                .build();
        //mysql更新
        cinemaMapper.updateById(cinema);
        //redis清除对应geo缓存
        redisTemplate.opsForGeo().remove(RedisConstant.CINEMA_GEO, cinema.getId());
        //redis写入对应geo缓存
        Point point = new Point(cinema.getLongitude().doubleValue(), cinema.getLatitude().doubleValue());
        redisTemplate.opsForGeo().add(RedisConstant.CINEMA_GEO, point, cinema.getId());
        //清除缓存
        redisTemplate.delete(RedisConstant.CINEMA_LIST);
    }

    @Override
    public CinemaQueryVO query(Long id) {
        Cinema cinema = cinemaMapper.selectById(id);
        if (cinema == null) {
            //影院不存在
            throw new BusinessException(RetEnum.NOT_FOUND);
        }
        return CinemaQueryVO.builder()
                .id(cinema.getId())
                .name(cinema.getName())
                .address(cinema.getAddress())
                .longitude(cinema.getLongitude())
                .latitude(cinema.getLatitude())
                .phone(cinema.getPhone())
                .tags(cinema.getTags())
                .build();
    }

    @Override
    public PageInfo<CinemaPageVO> page(CinemaPageDTO cinemaPageDTO) {
        PageHelper.startPage(cinemaPageDTO.getPageNum(), cinemaPageDTO.getPageSize());
        List<CinemaPageVO> list = cinemaMapper.selectPage(cinemaPageDTO);
        return PageInfo.of(list);
    }

    @Override
    public Long count() {
        return cinemaMapper.count();
    }

    @Override
    public List<CinemaListVO> list(BigDecimal longitude, BigDecimal latitude) {
        String key = RedisConstant.CINEMA_LIST;
        List<CinemaListVO> listVO;
        //redis查询
        List<CinemaListVO> listCache = redisUtil.getListByJson(key, CinemaListVO.class);
        if (listCache != null) {
            listVO = listCache;
        } else {
            //分布式锁防止缓存击穿
            String lockKey = "lock:" + key;
            RLock lock = redissonClient.getLock(lockKey);
            try {
                //尝试获取锁
                lock.lock();
                //双重检查
                listCache = redisUtil.getListByJson(key, CinemaListVO.class);
                if (listCache != null) {
                    listVO = listCache;
                } else {
                    //缓存未命中，mysql查询
                    List<Cinema> listDB = cinemaMapper.selectList();
                    if (CollectionUtils.isEmpty(listDB)) {
                        return List.of();
                    }
                    //回写缓存
                    listVO = listDB.stream().map(cinema -> CinemaListVO.builder()
                            .id(cinema.getId())
                            .name(cinema.getName())
                            .address(cinema.getAddress())
                            .tags(cinema.getTags())
                            .build()).toList();
                    redisUtil.saveListToJson(key, listVO, RedisConstant.CINEMA_LIST_TTL, TimeUnit.MINUTES);
                }
            } finally {
                lock.unlock();
            }
        }
        //设置影院距离
        Map<Long, Double> distanceMap = getCinemaDistance(longitude, latitude);
        return listVO.stream().peek(item -> item.setDistance(distanceMap.get(item.getId()))).toList();
    }

    /**
     * 根据用户坐标获取影院距离
     *
     * @param longitude 经度
     * @param latitude  纬度
     * @return 影院距离Map
     */
    private Map<Long, Double> getCinemaDistance(BigDecimal longitude, BigDecimal latitude) {
        //创建地理参考对象
        Point userPoint = new Point(longitude.doubleValue(), latitude.doubleValue());
        GeoReference<Object> geoReference = GeoReference.fromCoordinate(userPoint);
        //查询半径（整个地球，地球周长40000km作为半径）
        Distance radius = new Distance(40000, RedisGeoCommands.DistanceUnit.KILOMETERS);
        //构建查询参数
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs
                .newGeoRadiusArgs()
                .includeDistance();
        //获取查询结果
        GeoResults<RedisGeoCommands.GeoLocation<Object>> results = redisTemplate.opsForGeo()
                .search(RedisConstant.CINEMA_GEO, geoReference, radius, args);
        //解析查询结果
        Map<Long, Double> distanceMap = new HashMap<>();
        if (results != null) {
            for (GeoResult<RedisGeoCommands.GeoLocation<Object>> result : results) {
                String cinemaIdStr = result.getContent().getName().toString();
                Long cinemaId = Long.valueOf(cinemaIdStr);
                Double distance = result.getDistance().getValue();
                distanceMap.put(cinemaId, distance);
            }
        }
        return distanceMap;
    }

    @Override
    public CinemaDetailVO detail(Long cinemaId) {
        //redis查询
        String key = RedisConstant.CINEMA + ":" + cinemaId;
        CinemaDetailVO detailCache = redisUtil.getObjByJson(key, CinemaDetailVO.class);
        if (detailCache != null) {
            //检查是否是空值缓存
            if (detailCache.getId() == null) {
                return null;
            }
            return detailCache;
        }
        //缓存重建期间，使用Redisson分布式锁防御缓存击穿
        String lockKey = "lock:" + key;
        RLock lock = redissonClient.getLock(lockKey);
        try {
            //尝试获取锁
            lock.lock();
            //双重检查
            detailCache = redisUtil.getObjByJson(key, CinemaDetailVO.class);
            if (detailCache != null) {
                //检查是否是空值缓存
                if (detailCache.getId() == null) {
                    return null;
                }
                return detailCache;
            }
            //缓存未命中，mysql查询
            CinemaDetailVO detailVO = cinemaMapper.selectDetail(cinemaId);
            if (detailVO != null) {
                //回写缓存
                redisUtil.saveObjToJson(key, detailVO, RedisConstant.CINEMA_DETAIL_TTL, TimeUnit.MINUTES);
                return detailVO;
            } else {
                //缓存空值，防止缓存穿透
                redisUtil.saveEmpty(key);
                return null;
            }
        } finally {
            lock.unlock();
        }
    }
}
