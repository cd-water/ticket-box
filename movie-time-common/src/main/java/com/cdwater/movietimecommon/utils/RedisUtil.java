package com.cdwater.movietimecommon.utils;

import com.alibaba.fastjson2.JSON;
import com.cdwater.movietimecommon.constants.RedisConstant;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.MapUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具
 */
@Component
public class RedisUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public <T> List<T> getListByJson(String key, Class<T> clazz) {
        Object json = redisTemplate.opsForValue().get(key);
        if (json == null) {
            //未命中缓存
            return null;
        }
        return JSON.parseArray(json.toString(), clazz);
    }

    public <T> void saveListToJson(String key, List<T> list, long ttl, TimeUnit timeUnit) {
        String json = JSON.toJSONString(list);
        redisTemplate.opsForValue().set(key, json, ttl, timeUnit);
    }

    public <T> T getObjByJson(String key, Class<T> clazz) {
        Object json = redisTemplate.opsForValue().get(key);
        if (json == null) {
            //未命中缓存
            return null;
        }
        if (json.toString().equals(RedisConstant.EMPTY)) {
            //命中空缓存
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return JSON.parseObject(json.toString(), clazz);
    }

    public <T> void saveObjToJson(String key, T obj, long ttl, TimeUnit timeUnit) {
        String json = JSON.toJSONString(obj);
        redisTemplate.opsForValue().set(key, json, ttl, timeUnit);
    }

    public void saveEmpty(String key) {
        redisTemplate.opsForValue().set(key, RedisConstant.EMPTY, RedisConstant.EMPTY_TTL, TimeUnit.MINUTES);
    }

    public <T> T getObjByHash(String key, Class<T> clazz) {
        Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
        if (MapUtils.isEmpty(map)) {
            //未命中缓存
            return null;
        }
        return JSON.parseObject(JSON.toJSONString(map), clazz);
    }

    public <T> void saveObjToHash(String key, T obj, long ttl, TimeUnit timeUnit) {
        String json = JSON.toJSONString(obj);
        Map<String, Object> map = JSON.parseObject(json, Map.class);
        redisTemplate.opsForHash().putAll(key, map);
        redisTemplate.expire(key, ttl, timeUnit);
    }

    public void saveEmptyHash(String key) {
        Map<String, Object> emptyMap = new HashMap<>();
        emptyMap.put(RedisConstant.EMPTY, RedisConstant.EMPTY);
        redisTemplate.opsForHash().putAll(key, emptyMap);
        redisTemplate.expire(key, RedisConstant.EMPTY_TTL, TimeUnit.MINUTES);
    }

    public void delete(String... key) {
        redisTemplate.delete(List.of(key));
    }
}
