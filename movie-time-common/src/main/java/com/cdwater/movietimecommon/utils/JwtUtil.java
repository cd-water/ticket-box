package com.cdwater.movietimecommon.utils;

import com.cdwater.movietimecommon.constants.RedisConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Jwt工具
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.refresh-token-ttl}")
    private long refreshTokenTtl;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 生成token
     *
     * @param id       主题
     * @param claims   载荷
     * @param tokenTTL token有效期
     * @return 生成的token
     */
    public String generateToken(Long id, Map<String, Object> claims, long tokenTTL) {
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setClaims(claims)
                .setId(UUID.randomUUID().toString())
                .setSubject(id.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tokenTTL))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析token
     *
     * @param token 要解析的token
     * @return 解析后的载荷
     */
    public Claims parseToken(String token) {
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 解析token（即使过期）
     *
     * @param token 要解析的token
     * @return 解析后的载荷
     */
    public Claims parseTokenAllowExpired(String token) {
        try {
            return parseToken(token);
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    /**
     * redis保存刷新token
     *
     * @param userId 持有用户id
     * @param token  刷新token
     */
    public void saveRefreshToken(Long userId, String token) {
        String key = RedisConstant.TOKEN_REFRESH + ":" + userId;
        redisTemplate.opsForValue().set(key, token, refreshTokenTtl, TimeUnit.MILLISECONDS);
    }

    /**
     * redis获取刷新token
     *
     * @param userId 持有用户id
     * @return 刷新token
     */
    public String getRefreshToken(Long userId) {
        String key = RedisConstant.TOKEN_REFRESH + ":" + userId;
        return (String) redisTemplate.opsForValue().get(key);
    }

    /**
     * redis删除刷新token
     *
     * @param userId 持有用户id
     */
    public void removeRefreshToken(Long userId) {
        String key = RedisConstant.TOKEN_REFRESH + ":" + userId;
        redisTemplate.delete(key);
    }

    /**
     * 将token加入黑名单
     *
     * @param jti      jwt唯一标识
     * @param ttl      过期时间
     * @param timeUnit 时间单位
     */
    public void joinTokenBlackList(String jti, long ttl, TimeUnit timeUnit) {
        String key = RedisConstant.TOKEN_BLACKLIST + ":" + jti;
        redisTemplate.opsForValue().set(key, "", ttl, timeUnit);
    }

    /**
     * 判断token是否在黑名单
     *
     * @param jti jwt唯一标识
     * @return true:在黑名单 false:不在黑名单
     */
    public boolean existTokenBlackList(String jti) {
        String key = RedisConstant.TOKEN_BLACKLIST + ":" + jti;
        return redisTemplate.hasKey(key);
    }
}
