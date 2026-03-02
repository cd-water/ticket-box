package com.cdwater.movietimemovie.service.impl;

import com.cdwater.movietimecommon.constants.RedisConstant;
import com.cdwater.movietimecommon.utils.JwtUtil;
import com.cdwater.movietimecommon.utils.UserContext;
import com.cdwater.movietimemovie.mapper.ReviewMapper;
import com.cdwater.movietimemovie.model.dto.ReviewAddDTO;
import com.cdwater.movietimemovie.model.dto.ReviewPageDTO;
import com.cdwater.movietimemovie.model.entity.Review;
import com.cdwater.movietimemovie.model.vo.ReviewPageVO;
import com.cdwater.movietimemovie.model.vo.ReviewVO;
import com.cdwater.movietimemovie.mq.MovieProducer;
import com.cdwater.movietimemovie.service.ReviewService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Resource
    private ReviewMapper reviewMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private static final DefaultRedisScript<Long> LIKE_SCRIPT;

    static {
        LIKE_SCRIPT = new DefaultRedisScript<>();
        LIKE_SCRIPT.setLocation(new ClassPathResource("lua/like.lua"));
        LIKE_SCRIPT.setResultType(Long.class);
    }

    @Resource
    private MovieProducer movieProducer;

    @Resource
    private JwtUtil jwtUtil;

    @Override
    public PageInfo<ReviewPageVO> page(ReviewPageDTO reviewPageDTO) {
        PageHelper.startPage(reviewPageDTO.getPageNum(), reviewPageDTO.getPageSize());
        List<ReviewPageVO> list = reviewMapper.selectPage(reviewPageDTO);
        return PageInfo.of(list);
    }

    @Override
    public void goDead(Long id) {
        reviewMapper.goDead(id);
    }

    @Override
    public void goAlive(Long id) {
        reviewMapper.goAlive(id);
    }

    @Override
    public void add(ReviewAddDTO reviewAddDTO) {
        Review review = Review.builder()
                .userId(UserContext.getId())
                .movieId(reviewAddDTO.getMovieId())
                .content(reviewAddDTO.getContent())
                .parentId(reviewAddDTO.getParentId())
                .build();
        reviewMapper.insert(review);
    }

    @Override
    public List<ReviewVO> showMovieReview(Long movieId, Long parentId, String authHeader) {
        List<ReviewVO> list = reviewMapper.selectByMovieIdAndParentId(movieId, parentId);
        return fillReviewList(list, authHeader);
    }

    @Override
    public List<ReviewVO> showUserReview(Long userId, String authHeader) {
        List<ReviewVO> list = reviewMapper.selectByUserId(userId);
        return fillReviewList(list, authHeader);
    }

    private List<ReviewVO> fillReviewList(List<ReviewVO> list, String authHeader) {
        //是否登录
        Long curUserId = checkLogin(authHeader);
        //redis管道补充数据
        List<Object> results = redisTemplate.executePipelined(new SessionCallback<>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                for (ReviewVO item : list) {
                    //点赞数、用户是否点赞
                    String likeUserKey = RedisConstant.REVIEW_LIKE + ":" + item.getId();
                    operations.opsForSet().size(likeUserKey);
                    if (curUserId != null) {
                        operations.opsForSet().isMember(likeUserKey, curUserId.toString());
                    }
                }
                return null;
            }
        });
        //填充结果
        int index = 0;
        for (ReviewVO item : list) {
            //点赞数
            Object likeCount = results.get(index++);
            item.setLikeCount(likeCount != null ? ((Long) likeCount).intValue() : 0);
            //用户是否点赞
            if (curUserId != null) {
                Boolean hasLiked = (Boolean) results.get(index++);
                item.setHasLiked(hasLiked);
            } else {
                item.setHasLiked(false);
            }
        }
        //返回结果
        return list;
    }

    private Long checkLogin(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        try {
            //解析accessToken
            String accessToken = authHeader.substring(7);
            Claims claims = jwtUtil.parseToken(accessToken);
            //检查是否在Redis黑名单
            String jti = claims.getId();
            if (jwtUtil.existTokenBlackList(jti)) {
                return null;
            }
            return Long.valueOf(claims.getSubject());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void like(Long reviewId) {
        Long userId = UserContext.getId();
        //lua脚本执行点赞操作
        String likeUserKey = RedisConstant.REVIEW_LIKE + ":" + reviewId;
        Long result = redisTemplate.execute(
                LIKE_SCRIPT,
                List.of(likeUserKey),
                userId.toString()
        );
        //异步持久化到mysql
        movieProducer.persistLike(userId, reviewId, result);
    }
}
