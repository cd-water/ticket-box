package com.cdwater.movietimeuser.service.impl;

import com.cdwater.movietimecommon.constants.RedisConstant;
import com.cdwater.movietimecommon.constants.TextConstant;
import com.cdwater.movietimecommon.enums.RetEnum;
import com.cdwater.movietimecommon.exceptions.BusinessException;
import com.cdwater.movietimecommon.utils.AdminContext;
import com.cdwater.movietimecommon.utils.JwtUtil;
import com.cdwater.movietimecommon.utils.UserContext;
import com.cdwater.movietimeuser.mapper.AdminMapper;
import com.cdwater.movietimeuser.mapper.UserMapper;
import com.cdwater.movietimeuser.model.dto.*;
import com.cdwater.movietimeuser.model.entity.Admin;
import com.cdwater.movietimeuser.model.entity.User;
import com.cdwater.movietimeuser.model.vo.AdminLoginRes;
import com.cdwater.movietimeuser.model.vo.AppLoginRes;
import com.cdwater.movietimeuser.mq.UserProducer;
import com.cdwater.movietimeuser.service.AuthService;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    private AdminMapper adminMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private JwtUtil jwtUtil;

    @Value("${jwt.admin-token-ttl}")
    private long adminTokenTtl;

    @Value("${jwt.access-token-ttl}")
    private long accessTokenTtl;

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private static final DefaultRedisScript<Long> SEND_CODE_SCRIPT;

    static {
        SEND_CODE_SCRIPT = new DefaultRedisScript<>();
        SEND_CODE_SCRIPT.setLocation(new ClassPathResource("lua/send-code.lua"));
        SEND_CODE_SCRIPT.setResultType(Long.class);
    }

    @Resource
    private UserProducer userProducer;

    @Override
    public AdminLoginRes adminLogin(AdminLoginDTO adminLoginDTO) {
        Admin admin = adminMapper.selectByUsername(adminLoginDTO.getUsername());
        if (admin == null || !passwordEncoder.matches(adminLoginDTO.getPassword(), admin.getPassword())) {
            //用户名或密码错误
            throw new BusinessException(RetEnum.LOGIN_FAIL);
        }
        //生成token
        Map<String, Object> claims = new HashMap<>();
        claims.put("top", admin.getTop());
        String token = jwtUtil.generateToken(admin.getId(), claims, adminTokenTtl);
        //封装返回对象
        return AdminLoginRes.builder()
                .id(admin.getId())
                .username(admin.getUsername())
                .top(admin.getTop())
                .token(token)
                .build();
    }

    @Override
    public void adminLogout(String header) {
        //获取token
        String token = header.substring(7);
        //获取剩余过期时间
        Claims claims = jwtUtil.parseToken(token);
        long remainingTime = claims.getExpiration().getTime() - System.currentTimeMillis();
        if (remainingTime <= 0) {
            return;
        }
        //加入redis黑名单
        String jti = claims.getId();
        jwtUtil.joinTokenBlackList(jti, remainingTime, TimeUnit.MILLISECONDS);
    }

    @Override
    public void adminChangePwd(AdminCpwdDTO adminCpwdDTO, String header) {
        //校验原密码
        Long id = AdminContext.getId();
        String passwordDB = adminMapper.selectPwdById(id);
        if (passwordDB == null || !passwordEncoder.matches(adminCpwdDTO.getOldPassword(), passwordDB)) {
            //原密码错误
            throw new BusinessException(RetEnum.OLD_PASSWORD_ERROR);
        }
        //新密码，BCrypt加密
        String newPassword = passwordEncoder.encode(adminCpwdDTO.getNewPassword());
        adminMapper.updatePwdById(id, newPassword);
        //重新登录
        adminLogout(header);
    }

    @Override
    public void appSendCode(SendCodeDTO sendCodeDTO) {
        String codeType = sendCodeDTO.getCodeType();
        String phone = sendCodeDTO.getPhone();
        String key = RedisConstant.CODE + ":" + codeType + ":" + phone;
        String code = RandomStringUtils.randomNumeric(6);
        //lua脚本短信防刷+保存验证码
        Long result = redisTemplate.execute(SEND_CODE_SCRIPT, List.of(key), code, RedisConstant.CODE_TTL);
        if (result == 1) {
            throw new BusinessException(RetEnum.CODE_FREQUENCY);
        }
        //rabbitmq异步发送验证码
        userProducer.sendCode(phone, code);
    }

    @Override
    public AppLoginRes appPhoneLogin(AppLoginDTO appLoginDTO) {
        String phone = appLoginDTO.getPhone();
        String code = appLoginDTO.getCode();
        //redis校验验证码
        String key = RedisConstant.CODE + ":" + RedisConstant.LOGIN + ":" + phone;
        String codeCache = (String) redisTemplate.opsForValue().get(key);
        //验证码已过期 | 错误
        if (codeCache == null || !StringUtils.equals(codeCache, code)) {
            throw new BusinessException(RetEnum.CODE_ERROR);
        }
        //删除redis中的验证码，避免重复使用
        redisTemplate.delete(key);
        //mysql查询用户
        User user = userMapper.selectByPhone(phone);
        if (user == null) {
            //用户不存在，静默注册
            user = User.builder()
                    .phone(phone)
                    .nickname(TextConstant.DEFAULT_NICKNAME_PREFIX + RandomStringUtils.randomAlphabetic(6))
                    .avatar(endpoint + "/" + bucketName + "/" + TextConstant.DEFAULT_AVATAR)
                    .build();
            userMapper.insert(user);
        }
        //生成双Token
        AppLoginRes appLoginRes = generateTwoToken(user.getId(), phone);
        //返回双Token以及基本用户信息
        appLoginRes.setId(user.getId());
        appLoginRes.setNickname(user.getNickname());
        appLoginRes.setAvatar(user.getAvatar());
        return appLoginRes;
    }

    @Override
    public AppLoginRes appPwdLogin(AppLoginDTO appLoginDTO) {
        String phone = appLoginDTO.getPhone();
        String password = appLoginDTO.getPassword();
        //检查redis登录失败次数，防暴力破解
        String key = RedisConstant.LOGIN_FAIL + ":" + phone;
        Integer failCount = (Integer) redisTemplate.opsForValue().get(key);
        if (failCount != null && failCount >= 5) {
            throw new BusinessException(RetEnum.LOGIN_FREQUENT);
        }
        //mysql查询用户，验证登录
        User user = userMapper.selectByPhone(phone);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            //用户名或密码错误
            redisTemplate.opsForValue().increment(key);
            redisTemplate.expire(key, RedisConstant.LOGIN_FAIL_TTL, TimeUnit.MINUTES);
            throw new BusinessException(RetEnum.LOGIN_FAIL);
        }
        //登录成功，清除失败次数
        redisTemplate.delete(key);
        //生成双Token
        AppLoginRes appLoginRes = generateTwoToken(user.getId(), phone);
        //返回双Token以及基本用户信息
        appLoginRes.setId(user.getId());
        appLoginRes.setNickname(user.getNickname());
        appLoginRes.setAvatar(user.getAvatar());
        return appLoginRes;
    }

    @Override
    public void appChangePwd(AppCpwdDTO appCpwdDTO) {
        //新密码，BCrypt加密
        String newPassword = passwordEncoder.encode(appCpwdDTO.getNewPassword());
        //更新用户密码
        String phone = UserContext.getPhone();
        userMapper.updatePwdByPhone(phone, newPassword);
    }

    @Override
    public void appForgetPwd(AppFpwdDTO appFpwdDTO) {
        String phone = appFpwdDTO.getPhone();
        String code = appFpwdDTO.getCode();
        //redis校验验证码
        String key = RedisConstant.CODE + ":" + RedisConstant.FPWD + ":" + phone;
        String codeCache = (String) redisTemplate.opsForValue().get(key);
        //验证码已过期 | 错误
        if (codeCache == null || !StringUtils.equals(codeCache, code)) {
            throw new BusinessException(RetEnum.CODE_ERROR);
        }
        //删除redis中的验证码，避免重复使用
        redisTemplate.delete(key);
        //新密码，BCrypt加密
        String newPassword = passwordEncoder.encode(appFpwdDTO.getNewPassword());
        //更新用户密码
        userMapper.updatePwdByPhone(phone, newPassword);
    }

    @Override
    public void appLogout(String authHeader) {
        //获取accessToken
        String accessToken = authHeader.substring(7);
        //获取剩余过期时间
        Claims claims = jwtUtil.parseToken(accessToken);
        long remainingTime = claims.getExpiration().getTime() - System.currentTimeMillis();
        if (remainingTime <= 0) {
            return;
        }
        //将accessToken加入Redis黑名单
        String jti = claims.getId();
        jwtUtil.joinTokenBlackList(jti, remainingTime, TimeUnit.MILLISECONDS);
        //清除Redis中的refreshToken
        Long userId = Long.valueOf(claims.getSubject());
        jwtUtil.removeRefreshToken(userId);
    }

    @Override
    public AppLoginRes appRefreshToken(String authHeader, String refreshToken) {
        //请求头为空 | 格式错误
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BusinessException(RetEnum.UNAUTHORIZED);
        }
        //获取accessToken
        String accessToken = authHeader.substring(7);
        //解析accessToken
        Claims claims = jwtUtil.parseTokenAllowExpired(accessToken);
        Long userId = Long.valueOf(claims.getSubject());
        String phone = claims.get("phone", String.class);
        //获取Redis中的refreshToken
        String refreshTokenCache = jwtUtil.getRefreshToken(userId);
        //refreshToken不存在 | 错误
        if (refreshTokenCache == null || !StringUtils.equals(refreshTokenCache, refreshToken)) {
            throw new BusinessException(RetEnum.UNAUTHORIZED);
        }
        //生成新的双Token并返回
        return generateTwoToken(userId, phone);
    }

    private AppLoginRes generateTwoToken(Long userId, String phone) {
        //生成accessToken
        Map<String, Object> claims = new HashMap<>();
        claims.put("phone", phone);
        String accessToken = jwtUtil.generateToken(userId, claims, accessTokenTtl);
        //生成refreshToken并存入redis
        String refreshToken = UUID.randomUUID().toString();
        jwtUtil.saveRefreshToken(userId, refreshToken);
        //返回双Token
        return AppLoginRes.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
