package com.cdwater.movietimecommon.constants;

/**
 * Redis常量
 */
public class RedisConstant {

    public static final String EMPTY = "_empty_";
    public static final Integer EMPTY_TTL = 2;//minute

    /**
     * 认证相关
     */
    public static final String TOKEN_BLACKLIST = "token:blacklist";
    public static final String TOKEN_REFRESH = "token:refresh";
    public static final String CODE = "code";
    public static final Integer CODE_TTL = 300;//second
    public static final String LOGIN = "login";
    public static final String FPWD = "fpwd";
    public static final String LOGIN_FAIL = "login:fail";
    public static final Integer LOGIN_FAIL_TTL = 2;//minute

    /**
     * 用户相关
     */
    public static final String USER_FOLLOWER = "user:follower";
    public static final String USER_FAN = "user:fan";

    /**
     * 电影相关
     */
    public static final String MOVIE = "movie";
    public static final Integer MOVIE_DETAIL_TTL = 30;//minute
    public static final String MOVIE_SHOWING = "movie:showing";
    public static final String MOVIE_SOON = "movie:soon";
    public static final Integer MOVIE_LIST_TTL = 60;//minute、
    public static final String REVIEW_LIKE = "review:like";

    /**
     * 影院相关
     */
    public static final String CINEMA_GEO = "cinema:geo";
    public static final String CINEMA = "cinema";
    public static final Integer CINEMA_DETAIL_TTL = 30;//minute
    public static final String CINEMA_LIST = "cinema:list";
    public static final Integer CINEMA_LIST_TTL = 60;//minute
    public static final String SEAT_SCREENING = "seat:screening";
}
