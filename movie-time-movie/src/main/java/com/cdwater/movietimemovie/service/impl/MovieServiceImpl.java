package com.cdwater.movietimemovie.service.impl;

import com.cdwater.movietimecommon.constants.RedisConstant;
import com.cdwater.movietimecommon.constants.TextConstant;
import com.cdwater.movietimecommon.enums.RetEnum;
import com.cdwater.movietimecommon.exceptions.BusinessException;
import com.cdwater.movietimecommon.utils.RedisUtil;
import com.cdwater.movietimemovie.mapper.MovieMapper;
import com.cdwater.movietimemovie.model.dto.MovieAddDTO;
import com.cdwater.movietimemovie.model.dto.MoviePageDTO;
import com.cdwater.movietimemovie.model.dto.MovieUpdateDTO;
import com.cdwater.movietimemovie.model.entity.Movie;
import com.cdwater.movietimemovie.model.vo.*;
import com.cdwater.movietimemovie.service.MovieService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class MovieServiceImpl implements MovieService {

    @Resource
    private MovieMapper movieMapper;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private RedissonClient redissonClient;

    @Override
    public void add(MovieAddDTO movieAddDTO) {
        Movie movie = Movie.builder()
                .name(movieAddDTO.getName())
                .cover(movieAddDTO.getCover())
                .type(movieAddDTO.getType())
                .releaseDate(movieAddDTO.getReleaseDate())
                .duration(movieAddDTO.getDuration())
                .description(movieAddDTO.getDescription())
                .director(movieAddDTO.getDirector())
                .actors(movieAddDTO.getActors())
                .status(movieAddDTO.getStatus())
                .build();
        movieMapper.insert(movie);
        //清除缓存
        if (movieAddDTO.getStatus().equals(TextConstant.MOVIE_STATUS_SHOWING)) {
            redisUtil.delete(RedisConstant.MOVIE_SHOWING);
        } else if (movieAddDTO.getStatus().equals(TextConstant.MOVIE_STATUS_SOON)) {
            redisUtil.delete(RedisConstant.MOVIE_SOON);
        }
    }

    @Override
    public void update(MovieUpdateDTO movieUpdateDTO) {
        Movie movie = Movie.builder()
                .id(movieUpdateDTO.getId())
                .name(movieUpdateDTO.getName())
                .cover(movieUpdateDTO.getCover())
                .type(movieUpdateDTO.getType())
                .releaseDate(movieUpdateDTO.getReleaseDate())
                .duration(movieUpdateDTO.getDuration())
                .description(movieUpdateDTO.getDescription())
                .director(movieUpdateDTO.getDirector())
                .actors(movieUpdateDTO.getActors())
                .status(movieUpdateDTO.getStatus())
                .build();
        movieMapper.updateById(movie);
        //清除缓存
        redisUtil.delete(RedisConstant.MOVIE + ":" + movieUpdateDTO.getId(),
                RedisConstant.MOVIE_SHOWING,
                RedisConstant.MOVIE_SOON
        );
    }

    @Override
    public MovieQueryVO query(Long id) {
        Movie movie = movieMapper.selectById(id);
        if (movie == null) {
            //电影不存在
            throw new BusinessException(RetEnum.NOT_FOUND);
        }
        return MovieQueryVO.builder()
                .id(movie.getId())
                .name(movie.getName())
                .cover(movie.getCover())
                .type(movie.getType())
                .releaseDate(movie.getReleaseDate())
                .duration(movie.getDuration())
                .description(movie.getDescription())
                .director(movie.getDirector())
                .actors(movie.getActors())
                .status(movie.getStatus())
                .build();
    }

    @Override
    public PageInfo<MoviePageVO> page(MoviePageDTO moviePageDTO) {
        PageHelper.startPage(moviePageDTO.getPageNum(), moviePageDTO.getPageSize());
        List<MoviePageVO> list = movieMapper.selectPage(moviePageDTO);
        return PageInfo.of(list);
    }

    @Override
    public List<MovieShowingVO> showing() {
        //热映中
        Integer status = TextConstant.MOVIE_STATUS_SHOWING;
        return movieMapper.selectMapByStatus(status);
    }

    @Override
    public Long count() {
        return movieMapper.count();
    }

    @Override
    public List<MovieListVO> list(Integer status) {
        String key = null;
        if (TextConstant.MOVIE_STATUS_SHOWING.equals(status)) {
            key = RedisConstant.MOVIE_SHOWING;
        } else if (TextConstant.MOVIE_STATUS_SOON.equals(status)) {
            key = RedisConstant.MOVIE_SOON;
        } else {
            throw new BusinessException(RetEnum.BAD_REQUEST);
        }
        //redis查询
        List<MovieListVO> listCache = redisUtil.getListByJson(key, MovieListVO.class);
        if (listCache != null) {
            return listCache;
        }
        //分布式锁防止缓存击穿
        String lockKey = "lock:" + key;
        RLock lock = redissonClient.getLock(lockKey);
        try {
            //尝试获取锁
            lock.lock();//无限循环等待，保证查询成功
            //双重检查
            listCache = redisUtil.getListByJson(key, MovieListVO.class);
            if (listCache != null) {
                return listCache;
            }
            //缓存未命中，mysql查询
            List<Movie> listDB = movieMapper.selectListByStatus(status);
            if (CollectionUtils.isEmpty(listDB)) {
                return List.of();
            }
            //回写缓存
            List<MovieListVO> listVO = listDB.stream().map(movie -> MovieListVO.builder()
                    .id(movie.getId())
                    .name(movie.getName())
                    .cover(movie.getCover())
                    .director(movie.getDirector())
                    .actors(movie.getActors())
                    .rating(movie.getRating())
                    .build()).toList();
            redisUtil.saveListToJson(key, listVO, RedisConstant.MOVIE_LIST_TTL, TimeUnit.MINUTES);
            return listVO;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public MovieDetailVO detail(Long movieId) {
        //redis查询
        String key = RedisConstant.MOVIE + ":" + movieId;
        MovieDetailVO detailCache = redisUtil.getObjByHash(key, MovieDetailVO.class);
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
            lock.lock();//无限循环等待，保证查询成功
            //双重检查
            detailCache = redisUtil.getObjByHash(key, MovieDetailVO.class);
            if (detailCache != null) {
                //检查是否是空值缓存
                if (detailCache.getId() == null) {
                    return null;
                }
                return detailCache;
            }
            //缓存未命中，mysql查询
            Movie movieDB = movieMapper.selectById(movieId);
            if (movieDB != null) {
                //回写缓存
                MovieDetailVO detailVO = MovieDetailVO.builder()
                        .id(movieDB.getId())
                        .name(movieDB.getName())
                        .cover(movieDB.getCover())
                        .type(movieDB.getType())
                        .releaseDate(movieDB.getReleaseDate())
                        .duration(movieDB.getDuration())
                        .description(movieDB.getDescription())
                        .director(movieDB.getDirector())
                        .actors(movieDB.getActors())
                        .rating(movieDB.getRating())
                        .status(movieDB.getStatus())
                        .build();
                redisUtil.saveObjToHash(key, detailVO, RedisConstant.MOVIE_DETAIL_TTL, TimeUnit.MINUTES);
                return detailVO;
            } else {
                //缓存空值，防止缓存穿透
                redisUtil.saveEmptyHash(key);
                return null;
            }
        } finally {
            lock.unlock();
        }
    }
}
