package com.cdwater.movietimeuser.service.impl;

import com.cdwater.movietimecommon.constants.RedisConstant;
import com.cdwater.movietimecommon.enums.RetEnum;
import com.cdwater.movietimecommon.exceptions.BusinessException;
import com.cdwater.movietimecommon.utils.JwtUtil;
import com.cdwater.movietimecommon.utils.UserContext;
import com.cdwater.movietimeuser.mapper.UserMapper;
import com.cdwater.movietimeuser.model.dto.UserEditDTO;
import com.cdwater.movietimeuser.model.dto.UserPageDTO;
import com.cdwater.movietimeuser.model.entity.User;
import com.cdwater.movietimeuser.model.vo.UserDetailVO;
import com.cdwater.movietimeuser.model.vo.UserListVO;
import com.cdwater.movietimeuser.model.vo.UserPageVO;
import com.cdwater.movietimeuser.mq.UserProducer;
import com.cdwater.movietimeuser.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private UserProducer userProducer;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private static final DefaultRedisScript<Long> FOLLOW_SCRIPT;

    static {
        FOLLOW_SCRIPT = new DefaultRedisScript<>();
        FOLLOW_SCRIPT.setLocation(new ClassPathResource("lua/follow.lua"));
        FOLLOW_SCRIPT.setResultType(Long.class);
    }

    @Override
    public PageInfo<UserPageVO> page(UserPageDTO userPageDTO) {
        PageHelper.startPage(userPageDTO.getPageNum(), userPageDTO.getPageSize());
        List<UserPageVO> list = userMapper.selectPage(userPageDTO);
        return PageInfo.of(list);
    }

    @Override
    public Long count() {
        return userMapper.count();
    }

    @Override
    public UserDetailVO detail(Long userId, String authHeader) {
        //MySQL查询用户详情
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(RetEnum.NOT_FOUND);
        }
        //获取关注数和粉丝数
        Integer followerCount = getFollowerCount(userId);
        Integer fanCount = getFanCount(userId);
        //查询是否关注
        Boolean hasFollowed = checkFollowed(userId, authHeader);
        //封装用户详情VO
        return UserDetailVO.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .gender(user.getGender())
                .birthday(user.getBirthday())
                .introduce(user.getIntroduce())
                .fanCount(fanCount)
                .followerCount(followerCount)
                .hasFollowed(hasFollowed)
                .build();
    }

    private Integer getFollowerCount(Long userId) {
        String key = RedisConstant.USER_FOLLOWER + ":" + userId;
        Long size = redisTemplate.opsForSet().size(key);
        return size != null ? size.intValue() : 0;
    }

    private Integer getFanCount(Long userId) {
        String key = RedisConstant.USER_FAN + ":" + userId;
        Long size = redisTemplate.opsForSet().size(key);
        return size != null ? size.intValue() : 0;
    }

    private Boolean checkFollowed(Long userId, String authHeader) {
        //未登录
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }
        try {
            //解析accessToken
            String accessToken = authHeader.substring(7);
            Claims claims = jwtUtil.parseToken(accessToken);
            //检查是否在Redis黑名单
            String jti = claims.getId();
            if (jwtUtil.existTokenBlackList(jti)) {
                return false;
            }
            //检查是否查询自己
            Long curUserId = Long.valueOf(claims.getSubject());
            if (Objects.equals(curUserId, userId)) {
                return false;
            }
            //查询是否关注
            String key = RedisConstant.USER_FOLLOWER + ":" + curUserId;
            Boolean hasFollowed = redisTemplate.opsForSet().isMember(key, userId.toString());
            return BooleanUtils.isTrue(hasFollowed);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void follow(Long targetId) {
        Long userId = UserContext.getId();
        if (Objects.equals(targetId, userId)) {
            //不能操作自己
            throw new BusinessException(RetEnum.BAD_REQUEST);
        }
        //lua脚本执行关注/取关
        String followerKey = RedisConstant.USER_FOLLOWER + ":" + userId;
        String fanKey = RedisConstant.USER_FAN + ":" + targetId;
        Long result = redisTemplate.execute(
                FOLLOW_SCRIPT,
                List.of(followerKey, fanKey),
                String.valueOf(targetId),
                String.valueOf(userId)
        );
        //异步持久化到mysql
        userProducer.persistFollow(userId, targetId, result);
    }

    @Override
    public List<UserListVO> listFollower(Long userId) {
        //获取关注列表
        String key = RedisConstant.USER_FOLLOWER + ":" + userId;
        return getUserList(key);
    }

    @Override
    public List<UserListVO> listFan(Long userId) {
        //获取粉丝列表
        String key = RedisConstant.USER_FAN + ":" + userId;
        return getUserList(key);
    }

    private List<UserListVO> getUserList(String key) {
        Set<Object> members = redisTemplate.opsForSet().members(key);
        if (CollectionUtils.isEmpty(members)) {
            return List.of();
        }
        List<Long> ids = members.stream()
                .map(id -> Long.valueOf(id.toString()))
                .toList();
        //mysql查询
        return userMapper.selectByIds(ids);
    }

    @Override
    public void edit(UserEditDTO userEditDTO) {
        User user = User.builder()
                .id(UserContext.getId())
                .nickname(userEditDTO.getNickname())
                .avatar(userEditDTO.getAvatar())
                .gender(userEditDTO.getGender())
                .birthday(userEditDTO.getBirthday())
                .introduce(userEditDTO.getIntroduce())
                .build();
        userMapper.updateById(user);
    }
}
