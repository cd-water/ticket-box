package com.cdwater.movietimecommon.constants;

/**
 * RabbitMQ常量
 */
public class RabbitMqConstant {
    //业务交换机
    public static final String BUSINESS_EXCHANGE = "business.exchange";
    //死信交换机
    public static final String DLX_EXCHANGE = "dlx.exchange";

    //用户模块
    public static final String USER_CODE_QUEUE = "user.code.queue";
    public static final String USER_CODE_ROUTING_KEY = "user.code";
    public static final String USER_FOLLOW_QUEUE = "user.follow.queue";
    public static final String USER_FOLLOW_ROUTING_KEY = "user.follow";
    //电影模块
    public static final String MOVIE_REVIEWLIKE_QUEUE = "movie.reviewlike.queue";
    public static final String MOVIE_REVIEWLIKE_ROUTING_KEY = "movie.reviewlike";
    //订单模块
    public static final String ORDER_TTL_QUEUE = "order.ttl.queue";
    public static final String ORDER_TTL_ROUTING_KEY = "order.ttl";
    public static final String ORDER_RELEASE_DLQ = "order.release.dlq";
    public static final String ORDER_RELEASE_ROUTING_KEY = "order.release";
}
