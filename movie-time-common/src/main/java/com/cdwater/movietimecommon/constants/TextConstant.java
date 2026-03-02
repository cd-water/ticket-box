package com.cdwater.movietimecommon.constants;

/**
 * 文本常量
 */
public class TextConstant {

    /**
     * 默认超级管理员
     */
    public static final String DEFAULT_ADMIN_USERNAME = "admin";
    public static final String DEFAULT_ADMIN_PASSWORD = "Aa123456";
    public static final Integer TOP = 1;

    /**
     * 电影状态
     */
    public static final Integer MOVIE_STATUS_OVER = 0;
    public static final Integer MOVIE_STATUS_SHOWING = 1;
    public static final Integer MOVIE_STATUS_SOON = 2;

    /**
     * 订单状态
     */
    public static final Integer PENDING_PAYMENT = 0;
    public static final Integer PAID = 1;
    public static final Integer CANCELLED = 2;

    public static final Integer ORDER_TTL = 10;//minute

    public static final String DEFAULT_NICKNAME_PREFIX = "user_";
    public static final String DEFAULT_AVATAR = "default-avatar.png";
}
